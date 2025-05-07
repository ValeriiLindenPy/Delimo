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
import rs.delimo.common.client.UserClient;
import rs.delimo.common.exception.NotFoundException;
import rs.delimo.common.exception.OwnerException;
import rs.delimo.common.valueobject.ItemId;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.domain.Item;
import rs.delimo.item.domain.service.ImageManager;
import rs.delimo.item.infrastructure.mapper.ItemMapper;
import rs.delimo.item.infrastructure.repository.ItemRepository;
import rs.delimo.user.domain.User;

import static rs.delimo.item.domain.specification.ItemSpecifications.from;

import java.util.*;
import java.util.stream.Collectors;

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
    private final UserClient userClient;
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
        Item item = itemRepository.findById(new ItemId(itemId))
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %s not found".formatted(itemId));
                });
        log.warn("User id - {}", item.getOwner().value());
        ItemDto itemDto = mapper.toDto(item);
        itemDto.setOwner(userClient.findById(new UserId(item.getOwner().value())));
        log.warn("Retrieved item: {}", itemDto);
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
    @Transactional(readOnly = true)
    public ItemDto getByUserAndId(UUID id, User user) {
        log.info("Fetching item with id: {} for user: {}", id, user.getEmail());

        UserDto owner = userClient.findByEmail(user.getEmail());

        ItemDto itemDto = itemRepository.findOneByOwnerAndId(new UserId(owner.getId()), new ItemId(id))
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found for user id {}", id, owner.getId());
                    return new NotFoundException("Item with id - %s not found".formatted(id));
                });
        log.info("Retrieved item: {}", itemDto);
        itemDto.setOwner(owner);
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
    public ItemDto editOne(UUID itemId, ItemUpdateDto item, User user, List<MultipartFile> images, String existingImagesJson) {
        log.info("User {} is editing item with id {}", user.getEmail(), itemId);

        UserDto owner = userClient.findByEmail(user.getEmail());

        Item oldItem = itemRepository.findById(new ItemId(itemId))
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %s not found".formatted(itemId));
                });

        if (!Objects.equals(oldItem.getOwner().value(), owner.getId())) {
            log.error("Owner mismatch: item id {} does not belong to user id {}", itemId, owner.getId());
            throw new OwnerException("Item with id %s does not belong to user with id %s"
                    .formatted(itemId, owner.getId()));
        }

        owner = userClient.updateUserContactInfoByItemUpdate(item, owner);

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
        updatedItem.setOwner(owner);
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

        UserDto owner = userClient.findByEmail(user.getEmail());

        Item oldItem = itemRepository.findById(new ItemId(itemId))
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException("Item with id - %s not found".formatted(itemId));
                });

        if (!Objects.equals(oldItem.getOwner().value(), owner.getId())) {
            log.error("Owner mismatch: item id {} does not belong to user id {}", itemId, owner.getId());
            throw new OwnerException("Item with id %s does not belong to user with id %s"
                    .formatted(itemId, owner.getId()));
        }

        itemRepository.deleteById(new ItemId(itemId));
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

        Set<UserId> userIds = items.getContent().stream().map(Item::getOwner)
                .collect(Collectors.toSet());

        Map<UserId, UserDto> users = userClient.findByIds(userIds);

        return new ItemPageResponse(
                pageResponse,
                items.getContent()
                        .stream()
                        .map(item -> {
                            ItemDto dto = mapper.toDto(item);
                            dto.setOwner(users.get(item.getOwner()));
                            return dto;
                        })
                        .toList()
        );
    }

    @Override
    public ItemPageResponse getAll(User user, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<Item> items = itemRepository.findAllByOwner(user.getId(), pageable);

        PageResponse pageResponse = new PageResponse()
                .pageNumber(items.getNumber())
                .pageSize(items.getSize())
                .totalPages(items.getTotalPages())
                .totalElements(items.getTotalElements())
                .hasNext(items.hasNext());

        Set<UserId> userIds = items.getContent().stream().map(Item::getOwner)
                .collect(Collectors.toSet());

        Map<UserId, UserDto> users = userClient.findByIds(userIds);

        return new ItemPageResponse(
                pageResponse,
                items.getContent()
                        .stream()
                        .map(item -> {
                            ItemDto dto = mapper.toDto(item);
                            dto.setOwner(users.get(item.getOwner()));
                            return dto;
                        })
                        .toList()
        );
    }

    @Override
    public ItemPageResponse listItems(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<Item> items = itemRepository.findAll(pageable);

        PageResponse pageResponse = new PageResponse()
                .pageNumber(items.getNumber())
                .pageSize(items.getSize())
                .totalPages(items.getTotalPages())
                .totalElements(items.getTotalElements())
                .hasNext(items.hasNext());

        Set<UserId> userIds = items.getContent().stream().map(Item::getOwner)
                .collect(Collectors.toSet());

        Map<UserId, UserDto> users = userClient.findByIds(userIds);

        return new ItemPageResponse(
                pageResponse,
                items.getContent()
                        .stream()
                        .map(item -> {
                            ItemDto dto = mapper.toDto(item);
                            dto.setOwner(users.get(item.getOwner()));
                            return dto;
                        })
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

        UserDto owner = userClient.findByEmail(user.getEmail());

        owner = userClient.updateUserContactInfoByItemCreate(item, owner);

        log.warn("Starting image upload for new item");
        List<String> imageUrls = imageManager.uploadImages(images);
        log.info("Uploaded {} images", imageUrls.size());

        Item newItem = mapper.toEntity(item);
        newItem.setImages(imageUrls);
        newItem.setAvailable(true);
        newItem.setOwner(new UserId(owner.getId()));

        ItemDto createdItem = mapper.toDto(itemRepository.save(newItem));
        log.info("Item created successfully with id {}", createdItem.getId());
        createdItem.setOwner(owner);
        return createdItem;
    }
}
