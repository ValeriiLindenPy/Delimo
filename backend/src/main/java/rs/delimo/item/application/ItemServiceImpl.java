package rs.delimo.item.application;

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
import rs.delimo.api.dto.*;
import rs.delimo.common.exception.NotFoundException;
import rs.delimo.common.exception.OwnerException;
import rs.delimo.item.domain.Item;
import rs.delimo.item.domain.service.ImageManager;
import rs.delimo.item.infrastructure.mapper.ItemMapper;
import rs.delimo.item.infrastructure.repository.ItemRepository;
import rs.delimo.user.domain.User;
import rs.delimo.user.infrastructure.repository.UserRepository;

import static rs.delimo.item.domain.specification.ItemSpecifications.from;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of the {@link ItemService} interface.
 * <p>
 * This service provides methods to manage items, including creating, updating,
 * deleting, and searching for items. It also handles image uploads and updates
 * the owner's contact information if necessary.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper mapper;
    private final UserRepository userRepository;
    private final ImageManager imageManager;
    private final ObjectMapper objectMapper;


    /**
     * Retrieves an item by its ID.
     *
     * @param itemId the ID of the item to retrieve
     * @return the {@link ItemDto} for the specified item
     * @throws NotFoundException if no item with the given ID is found
     */
    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(UUID itemId) {
        log.info("Fetching item with id: {}", itemId);
        ItemDto itemDto = itemRepository.findByIdValue(itemId)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %s not found".formatted(itemId));
                });
        log.debug("Retrieved item: {}", itemDto);
        return itemDto;
    }

    /**
     * Retrieves an item by its ID for a specific user.
     *
     * @param id   the ID of the item to retrieve
     * @param user the user who owns the item
     * @return the {@link ItemDto} for the specified item owned by the user
     * @throws NotFoundException if the user or item is not found
     */
    @Override
    public ItemDto getByUserAndId(UUID id, User user) {
        log.info("Fetching item with id: {} for user: {}", id, user.getEmail());
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });
        ItemDto itemDto = itemRepository.findOneByOwnerValueAndIdValue(owner.getId().value(), id)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found for user id {}", id, owner.getId());
                    return new NotFoundException("Item with id - %s not found".formatted(id));
                });
        log.info("Retrieved item: {}", itemDto);
        return itemDto;
    }

    /**
     * Edits an existing item.
     * <p>
     * The method updates the item details, uploads new images, merges them with
     * existing images, and updates the owner's contact information if necessary.
     * </p>
     *
     * @param itemId             the ID of the item to be updated
     * @param item               the updated item data as {@link ItemRequestDto}
     * @param user               the user attempting the update
     * @param images             the new images to be uploaded for the item
     * @param existingImagesJson a JSON string representing the list of existing image URLs
     * @return the updated {@link ItemDto}
     * @throws NotFoundException if the user or item is not found
     * @throws OwnerException    if the item does not belong to the user
     */
    @Override
    @Transactional
    public ItemDto editOne(UUID itemId, ItemRequestDto item, User user, List<MultipartFile> images, String existingImagesJson) {
        log.info("User {} is editing item with id {}", user.getEmail(), itemId);
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });

        Item oldItem = itemRepository.findByIdValue(itemId)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %s not found".formatted(itemId));
                });

        if (!Objects.equals(oldItem.getOwner().value(), owner.getId().value())) {
            log.error("Owner mismatch: item id {} does not belong to user id {}", itemId, owner.getId());
            throw new OwnerException("Item with id %s does not belong to user with id %s"
                    .formatted(itemId, owner.getId()));
        }

        updateUserContactInfo(item, owner);

        List<String> existingImageUrls = new ArrayList<>();
        if (existingImagesJson != null && !existingImagesJson.isBlank()) {
            try {
                existingImageUrls = objectMapper.readValue(existingImagesJson, List.class);
                log.info("Parsed {} existing image URLs", existingImageUrls.size());
            } catch (Exception e) {
                log.error("Error parsing existingImages JSON: {}", e.getMessage());
            }
        }

        List<String> newImageUrls = imageManager.uploadImages(images);
        log.info("Uploaded {} new images", newImageUrls.size());

        List<String> combinedImageUrls = new ArrayList<>(existingImageUrls);
        combinedImageUrls.addAll(newImageUrls);
        log.debug("Combined image URLs: {}", combinedImageUrls);

        if (!combinedImageUrls.isEmpty()) {
            oldItem.setImages(combinedImageUrls);
        }

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

        if (item.getPricePerDay() != null && !item.getPricePerDay().equals(oldItem.getPricePerDay())) {
            oldItem.setPricePerDay(item.getPricePerDay());
        }

        if (item.getMaxPeriodDays() != null && !item.getMaxPeriodDays().equals(oldItem.getMaxPeriodDays())) {
            oldItem.setMaxPeriodDays(item.getMaxPeriodDays());
        }

        ItemDto updatedItem = mapper.toDto(itemRepository.save(oldItem));
        log.info("Item with id {} updated successfully", itemId);
        return updatedItem;
    }

    /**
     * Deletes an item.
     *
     * @param itemId the ID of the item to delete
     * @param user   the user requesting deletion
     * @throws NotFoundException if the user or item is not found
     * @throws OwnerException    if the item does not belong to the user
     */
    @Override
    @Transactional
    public void delete(UUID itemId, User user) {
        log.info("User {} requested deletion of item with id {}", user.getEmail(), itemId);
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", user.getEmail());
                    return new NotFoundException("User not found");
                });
        Item oldItem = itemRepository.findByIdValue(itemId)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %s not found".formatted(itemId));
                });

        if (!Objects.equals(oldItem.getOwner().value(), owner.getId().value())) {
            log.error("Owner mismatch: item id {} does not belong to user id {}", itemId, owner.getId());
            throw new OwnerException("Item with id %s does not belong to user with id %s"
                    .formatted(itemId, owner.getId()));
        }

        itemRepository.deleteByIdValue(itemId);
        log.info("Item with id {} deleted successfully", itemId);
    }

    /**
     * Searches for item titles containing the specified query text.
     *
     * @param q     the query string to search for within item titles
     * @param limit the maximum number of titles to return
     * @return a list of {@link ItemTitle} objects that match the query
     */
    @Override
    public List<ItemTitle> searchTitles(String q, int limit) {
        log.info("Searching for item titles with query '{}' and limit {}", q, limit);
        Pageable pageable = PageRequest.of(0, limit);
        List<Item> items = itemRepository.findByTitleContainingIgnoreCase(q, pageable);
        log.info("Found {} titles matching query '{}'", items.size(), q);
        return items.stream().map(mapper::toItemTitle).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemPageResponse search(Integer page, Integer size, ItemFilterDto filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<Item> items = itemRepository.findAll(from(filter), pageable);

        PageResponse pageResponse = new PageResponse()
                .pageNumber(items.getNumber())
                .pageSize(items.getSize())
                .totalPages(items.getTotalPages())
                .totalElements(items.getTotalElements())
                .hasNext(items.hasNext());

        return new ItemPageResponse(
                pageResponse,
                items.getContent()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Override
    public ItemPageResponse getAll(User user, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<Item> items = itemRepository.findAllByOwnerValue(user.getId().value(), pageable);

        PageResponse pageResponse = new PageResponse()
                .pageNumber(items.getNumber())
                .pageSize(items.getSize())
                .totalPages(items.getTotalPages())
                .totalElements(items.getTotalElements())
                .hasNext(items.hasNext());

        return new ItemPageResponse(
                pageResponse,
                items.getContent()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }


    /**
     * Creates a new item.
     * <p>
     * This method creates a new item, uploads the provided images, sets the item as available,
     * assigns the current user as the owner, and updates the owner's contact information if missing.
     * </p>
     *
     * @param item   the item data as {@link ItemRequestDto}
     * @param user   the user creating the item
     * @param images the images to be uploaded for the item
     * @return the created {@link ItemDto}
     * @throws NotFoundException if the user is not found
     */
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

        log.warn("Starting image upload for new item");
        List<String> imageUrls = imageManager.uploadImages(images);
        log.info("Uploaded {} images", imageUrls.size());

        Item newItem = mapper.toEntity(item);
        newItem.setImages(imageUrls);
        newItem.setAvailable(true);
        newItem.setOwner(owner.getId());

        ItemDto createdItem = mapper.toDto(itemRepository.save(newItem));
        log.info("Item created successfully with id {}", createdItem.getId());
        return createdItem;
    }

    /**
     * Updates the owner's contact information if it is not already set.
     *
     * @param item  the item data containing contact information
     * @param owner the owner whose contact information should be updated
     */
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
}
