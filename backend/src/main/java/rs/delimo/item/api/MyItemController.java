package rs.delimo.item.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.api.controller.MyItemsApi;
import rs.delimo.api.dto.ItemDto;
import rs.delimo.api.dto.ItemPageResponse;
import rs.delimo.api.dto.ItemRequestDto;
import rs.delimo.api.dto.ItemUpdateDto;
import rs.delimo.common.client.SecurityClient;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.application.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyItemController implements MyItemsApi {
    private final ItemService service;
    private final SecurityClient securityClient;

    @Override
    public ResponseEntity<ItemDto> createMyItem(
            @RequestPart("item") ItemRequestDto item,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        UserId ownerId = new UserId(securityClient.getCurrentUserId());
        log.debug("Creating item for user {} with data: {}, images count: {}", ownerId, item, images != null ? images.size() : 0);
        ItemDto created = service.create(item, ownerId, images);
        log.debug("Created item: {}", created);
        return ResponseEntity.ok(created);
    }

    @Override
    public ResponseEntity<Void> deleteMyItem(UUID id) {
        UserId ownerId = new UserId(securityClient.getCurrentUserId());
        log.debug("Deleting item with id={} for user={}", id, ownerId);
        service.delete(id, ownerId);
        log.debug("Item deleted");
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ItemDto> getMyItem(UUID id) {
        UserId ownerId = new UserId(securityClient.getCurrentUserId());
        log.debug("Fetching item with id={} for user={}", id, ownerId);
        ItemDto item = service.getByUserAndId(id, ownerId);
        log.debug("Retrieved item: {}", item);
        return ResponseEntity.ok(item);
    }

    @Override
    public ResponseEntity<ItemPageResponse> listMyItems(Integer page, Integer size) {
        UserId ownerId = new UserId(securityClient.getCurrentUserId());
        log.debug("Listing items for user={}, page={}, size={}", ownerId, page, size);
        ItemPageResponse response = service.getAll(ownerId, page, size);
        log.debug("Retrieved {} items", response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ItemDto> updateMyItem(
            UUID id,
            @RequestPart("item") ItemUpdateDto item,
            @RequestPart(value = "existingImages", required = false) String existingImages,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        UserId ownerId = new UserId(securityClient.getCurrentUserId());
        log.debug("Updating item with id={} for user={}, data={}, existingImages={}, new images count={}",
                id, ownerId, item, existingImages, images != null ? images.size() : 0);
        ItemDto updated = service.editOne(id, item, ownerId, images, existingImages);
        log.debug("Updated item: {}", updated);
        return ResponseEntity.ok(updated);
    }
}
