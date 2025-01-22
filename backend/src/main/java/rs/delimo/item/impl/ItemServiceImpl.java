package rs.delimo.item.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import rs.delimo.item.Item;
import rs.delimo.item.ItemMapper;
import rs.delimo.item.ItemRepository;
import rs.delimo.item.ItemService;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final int MAX_IMAGES_COUNT = 5;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dotenv dotenv = Dotenv.load();
    private final String imgbbKey = dotenv.get("IMGBB_KEY");

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

        List<String> newImageUrls = processImages(images);

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

        List<String> imageUrls = processImages(images);

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


    private List<String> processImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            if (images.size() > MAX_IMAGES_COUNT) {
                log.error("illegal image count");
                throw new IllegalArgumentException("An item can have up to " + MAX_IMAGES_COUNT + " images.");
            }
            imageUrls = images.stream()
                    .map(this::uploadImageToImgbb)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return imageUrls;
    }

    private String uploadImageToImgbb(MultipartFile image) {
        try {
            String uploadUrl = "https://api.imgbb.com/1/upload";
            String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());
            LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("key", imgbbKey);
            formData.add("image", encodedImage);
            String response = restTemplate.postForObject(uploadUrl, formData, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("data").path("url").asText();
        } catch (Exception e) {
            log.error("Failed to upload image to Imgbb: {}", e.getMessage());
            return null;
        }
    }

    private void isUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id - %d not found".formatted(userId)));
    }
}
