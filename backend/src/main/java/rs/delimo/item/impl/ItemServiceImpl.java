package rs.delimo.item.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        log.info("Fetching all items: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<Item> itemsPage = itemRepository.findAllWithImages(pageable);
        log.info("Retrieved {} items", itemsPage.getTotalElements());
        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> getAll(String city, int page, int size) {
        log.info("Fetching items for city: '{}' - page={}, size={}", city, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<Item> itemsPage;

        if (city != null && !city.isBlank()) {
            itemsPage = itemRepository.findAllWithImagesByCity(city, pageable);
            log.info("City provided. Retrieved {} items for city '{}'", itemsPage.getTotalElements(), city);
        } else {
            itemsPage = itemRepository.findAllWithImages(pageable);
            log.info("No city provided. Retrieved {} items", itemsPage.getTotalElements());
        }

        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> getAllByOwner(int page, int size, User user) {
        log.info("Fetching items for owner: {} - page={}, size={}", user.getEmail(), page, size);
        Pageable pageable = PageRequest.of(page, size);
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });
        Page<Item> itemsPage = itemRepository.findAllByOwnerWithImages(pageable, owner.getId());
        log.info("Owner {} has {} items", owner.getEmail(), itemsPage.getTotalElements());
        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(Long itemId) {
        log.info("Fetching item with id: {}", itemId);
        ItemDto itemDto = itemRepository.findByIdWithImages(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %d not found".formatted(itemId));
                });
        log.info("Retrieved item: {}", itemDto);
        return itemDto;
    }

    @Override
    public ItemDto getByUserAndId(Long id, User user) {
        log.info("Fetching item with id: {} for user: {}", id, user.getEmail());
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });
        ItemDto itemDto = itemRepository.findOneByUserIdAndItemId(owner.getId(), id)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found for user id {}", id, owner.getId());
                    return new NotFoundException("Item with id - %d not found".formatted(id));
                });
        log.info("Retrieved item: {}", itemDto);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto editOne(Long itemId, ItemRequestDto item, User user, List<MultipartFile> images, String existingImagesJson) {
        log.info("User {} is editing item with id {}", user.getEmail(), itemId);
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %d not found".formatted(itemId));
                });

        if (!Objects.equals(oldItem.getOwner().getId(), owner.getId())) {
            log.error("Owner mismatch: item id {} does not belong to user id {}", itemId, owner.getId());
            throw new OwnerException("Item with id %d does not belong to user with id %d"
                    .formatted(itemId, owner.getId()));
        }

        // Update owner's contact info if necessary
        updateUserContactInfo(item, owner);

        // Process existing images JSON
        List<String> existingImageUrls = new ArrayList<>();
        if (existingImagesJson != null && !existingImagesJson.isBlank()) {
            try {
                existingImageUrls = objectMapper.readValue(existingImagesJson, List.class);
                log.info("Parsed {} existing image URLs", existingImageUrls.size());
            } catch (Exception e) {
                log.error("Error parsing existingImages JSON: {}", e.getMessage());
            }
        }

        // Upload new images
        List<String> newImageUrls = imageManager.uploadImages(images);
        log.info("Uploaded {} new images", newImageUrls.size());

        // Combine existing and new image URLs
        List<String> combinedImageUrls = new ArrayList<>(existingImageUrls);
        combinedImageUrls.addAll(newImageUrls);
        log.debug("Combined image URLs: {}", combinedImageUrls);

        if (!combinedImageUrls.isEmpty()) {
            oldItem.setImages(combinedImageUrls);
        }

        // Update fields if provided
        if (item.getTitle() != null && !item.getTitle().equals(oldItem.getTitle())) {
            log.debug("Updating title from '{}' to '{}'", oldItem.getTitle(), item.getTitle());
            oldItem.setTitle(item.getTitle());
        }
        if (item.getDescription() != null && !item.getDescription().equals(oldItem.getDescription())) {
            log.debug("Updating description");
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null && !item.getAvailable().equals(oldItem.getAvailable())) {
            log.debug("Updating availability from '{}' to '{}'", oldItem.getAvailable(), item.getAvailable());
            oldItem.setAvailable(item.getAvailable());
        }

        ItemDto updatedItem = ItemMapper.toItemDto(itemRepository.save(oldItem));
        log.info("Item with id {} updated successfully", itemId);
        return updatedItem;
    }

    @Override
    @Transactional
    public void delete(Long itemId, User user) {
        log.info("User {} requested deletion of item with id {}", user.getEmail(), itemId);
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %d not found".formatted(itemId));
                });

        if (!Objects.equals(oldItem.getOwner().getId(), owner.getId())) {
            log.error("Owner mismatch: item id {} does not belong to user id {}", itemId, owner.getId());
            throw new OwnerException("Item with id %d does not belong to user with id %d"
                    .formatted(itemId, owner.getId()));
        }

        itemRepository.deleteById(itemId);
        log.info("Item with id {} deleted successfully", itemId);
    }

    @Override
    public List<ItemTitle> searchTitles(String q, int limit) {
        log.info("Searching for item titles with query '{}' and limit {}", q, limit);
        Pageable pageable = PageRequest.of(0, limit);
        List<Item> items = itemRepository.findByTitleContainingIgnoreCase(q, pageable);
        log.info("Found {} titles matching query '{}'", items.size(), q);
        return items.stream().map(ItemMapper::toItemTitle).toList();
    }

    @Override
    public Page<ItemDto> searchByText(String text, int page, int pageSize, String city) {
        log.info("Searching for items with text '{}' and city '{}' - page={}, pageSize={}", text, city, page, pageSize);
        if (text == null || text.isBlank()) {
            log.warn("Search text is empty. Returning empty page.");
            return Page.empty();
        }
        log.debug("Executing search for text: {}", text);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created").descending());

        Page<Item> items;
        if (city != null && !city.isBlank()) {
            items = itemRepository.searchWithCity(city, text, pageable);
            log.info("Search with city '{}' returned {} items", city, items.getTotalElements());
        } else {
            items = itemRepository.search(pageable, text);
            log.info("Search without city returned {} items", items.getTotalElements());
        }
        return items.map(ItemMapper::toItemDto);
    }

    @Override
    @Transactional
    public ItemDto create(ItemRequestDto item, User user, List<MultipartFile> images) {
        log.info("User {} is creating a new item", user.getEmail());
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });

        updateUserContactInfo(item, owner);

        // Uncomment the following lines if email confirmation is required
        // if (!owner.getConfirmed()) {
        //     throw new ConfirmationException("Please confirm email");
        // }

        log.warn("Starting image upload for new item");
        List<String> imageUrls = imageManager.uploadImages(images);
        log.info("Uploaded {} images", imageUrls.size());

        Item newItem = ItemMapper.toItem(item);
        newItem.setImages(imageUrls);
        newItem.setAvailable(true);
        newItem.setOwner(owner);

        ItemDto createdItem = ItemMapper.toItemDto(itemRepository.save(newItem));
        log.info("Item created successfully with id {}", createdItem.getId());
        return createdItem;
    }

    private void updateUserContactInfo(ItemRequestDto item, User owner) {
        boolean updated = false;
        if (item.getCity() != null && (owner.getCity() == null || owner.getCity().isBlank())) {
            owner.setCity(item.getCity());
            updated = true;
            log.debug("Updated owner's city to '{}'", item.getCity());
        }
        if (item.getStreet() != null && (owner.getStreet() == null || owner.getStreet().isBlank())) {
            owner.setStreet(item.getStreet());
            updated = true;
            log.debug("Updated owner's street to '{}'", item.getStreet());
        }
        if (item.getViber() != null && (owner.getViber() == null || owner.getViber().isBlank())) {
            owner.setViber(item.getViber());
            updated = true;
            log.debug("Updated owner's Viber to '{}'", item.getViber());
        }
        if (item.getPhone() != null && (owner.getPhone() == null || owner.getPhone().isBlank())) {
            owner.setPhone(item.getPhone());
            updated = true;
            log.debug("Updated owner's phone to '{}'", item.getPhone());
        }
        if (updated) {
            userRepository.save(owner);
            log.info("Owner contact information updated for user {}", owner.getEmail());
        }
    }

    private void isUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new NotFoundException("User with id - %d not found".formatted(userId));
                });
    }
}
