package rs.delimo.item.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import rs.delimo.item.*;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.item.dto.ItemTitle;
import rs.delimo.service.ImageManager;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ImageManager imageManager;
    private final ObjectMapper objectMapper;


    @Override
    public Page<ItemDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemsPage = itemRepository.findAllWithImages(pageable);
        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> getAllByOwner(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size);
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Page<Item> itemsPage = itemRepository.findAllByOwnerWithImages(pageable, owner.getId());
        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(Long itemId) {
        return itemRepository.findByIdWithImages(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Item with id - %d not found".formatted(itemId)));
    }

    @Override
    public ItemDto getByUserAndId(Long id, User user) {
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        return itemRepository.findOneByUserIdAndItemId(owner.getId(), id)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Item with id - %d not found".formatted(id)));
    }

    @Override
    @Transactional
    public ItemDto editOne(Long itemId, ItemRequestDto item, User user, List<MultipartFile> images, String existingImagesJson) {
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id - %d not found".formatted(itemId)));

        if (!Objects.equals(oldItem.getOwner().getId(), owner.getId())) {
            throw new OwnerException("Item with id %d does not belong to user with id %d"
                    .formatted(itemId, owner.getId()));
        }

        updateUserContactInfo(item, owner);

        List<String> existingImageUrls = new ArrayList<>();
        if (existingImagesJson != null && !existingImagesJson.isBlank()) {
            try {
                // Используем ObjectMapper для парсинга JSON-массива
                existingImageUrls = objectMapper.readValue(existingImagesJson, List.class);
            } catch (Exception e) {
                log.error("Error parsing existingImages JSON: {}", e.getMessage());
            }
        }

        List<String> newImageUrls = imageManager.uploadImages(images);

        List<String> combinedImageUrls = new ArrayList<>(existingImageUrls);
        combinedImageUrls.addAll(newImageUrls);

        if (!combinedImageUrls.isEmpty()) {
            oldItem.setImages(combinedImageUrls);
        }

        if (item.getTitle() != null && !item.getTitle().equals(oldItem.getTitle())) {
            oldItem.setTitle(item.getTitle());
        }
        if (item.getDescription() != null && !item.getDescription().equals(oldItem.getDescription())) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null && !item.getAvailable().equals(oldItem.getAvailable())) {
            oldItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }

    @Override
    public void delete(Long itemId, User user) {
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id - %d not found".formatted(itemId)));

        if (!Objects.equals(oldItem.getOwner().getId(), owner.getId())) {
            throw new OwnerException("Item with id %d does not belong to user with id %d"
                    .formatted(itemId, owner.getId()));
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemTitle> searchTitles(String q, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Item> items = itemRepository.findByTitleContainingIgnoreCase(q, pageable);
        return items.stream().map(ItemMapper::toItemTitle).toList();
    }

    public Page<ItemDto> searchByText(String text, int page, int pageSize) {
        if (text == null || text.isBlank()) {
            return Page.empty();
        }
        log.debug("Executing search for text: {}", text);
        Pageable pageable = PageRequest.of(page, pageSize);
        return itemRepository.search(pageable, text).map(ItemMapper::toItemDto);
    }

    @Override
    @Transactional
    public ItemDto create(ItemRequestDto item, User user, List<MultipartFile> images) {
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        updateUserContactInfo(item, owner);

        //todo
//        if (!owner.getConfirmed()) {
//            throw new ConfirmationException("Please confirm email");
//        }

        log.warn("start image upload");
        List<String> imageUrls = imageManager.uploadImages(images);

        Item newItem = ItemMapper.toItem(item);
        newItem.setImages(imageUrls);
        newItem.setAvailable(true);
        newItem.setOwner(owner);

        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }


    private void updateUserContactInfo(ItemRequestDto item, User owner) {
        boolean updated = false;
        if (item.getCity() != null && (owner.getCity() == null || owner.getCity().isBlank())) {
            owner.setCity(item.getCity());
            updated = true;
        }
        if (item.getStreet() != null && (owner.getStreet() == null || owner.getStreet().isBlank())) {
            owner.setStreet(item.getStreet());
            updated = true;
        }
        if (item.getViber() != null && (owner.getViber() == null || owner.getViber().isBlank())) {
            owner.setViber(item.getViber());
            updated = true;
        }
        if (item.getPhone() != null && (owner.getPhone() == null || owner.getPhone().isBlank())) {
            owner.setPhone(item.getPhone());
            updated = true;
        }
        if (updated) {
            userRepository.save(owner);
        }
    }

    private void isUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id - %d not found".formatted(userId)));
    }
}
