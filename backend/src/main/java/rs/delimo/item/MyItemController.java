package rs.delimo.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.ValidationMarker;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.user.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/my-items")
@RequiredArgsConstructor
@Slf4j
public class MyItemController {
    private final ItemService itemService;
    private final PagedResourcesAssembler<ItemDto> assembler;

    @GetMapping
    public Map<String, Object> getAll(@AuthenticationPrincipal User user,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int size) {
        log.info("User {} requested all items: page={}, size={}", user.getUsername(), page, size);
        Page<ItemDto> items = itemService.getAllByOwner(page, size, user);
        log.info("Retrieved {} items for user {}", items.getTotalElements(), user.getUsername());

        PagedModel<EntityModel<ItemDto>> pagedModel = assembler.toModel(items);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", pagedModel.getMetadata());
        response.put("content", pagedModel.getContent().stream()
                .map(EntityModel::getContent)
                .toList());
        log.debug("Response for getAll: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    public ItemDto getOne(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.info("User {} requested item with id {}", user.getUsername(), id);
        ItemDto item = itemService.getByUserAndId(id, user);
        log.info("Item retrieved: {}", item);
        return item;
    }

    @PatchMapping(value = "/{itemId}", consumes = {"multipart/form-data"})
    public ItemDto editOne(
            @ModelAttribute @Validated(ValidationMarker.OnCreate.class) ItemRequestDto item,
            @RequestParam(value = "image", required = false) List<MultipartFile> images,
            @RequestParam(value = "existingImages", required = false) String existingImagesJson,
            @AuthenticationPrincipal User user,
            @PathVariable Long itemId
    ) {
        log.info("User {} is editing item with id {}", user.getUsername(), itemId);
        log.info("Received {} new images", images != null ? images.size() : 0);
        log.info("Existing images JSON: {}", existingImagesJson);
        ItemDto updatedItem = itemService.editOne(itemId, item, user, images, existingImagesJson);
        log.info("Item updated: {}", updatedItem);
        return updatedItem;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@ModelAttribute @Validated(ValidationMarker.OnCreate.class) ItemRequestDto item,
                          @RequestParam(value = "images", required = false) List<MultipartFile> images,
                          @AuthenticationPrincipal User user) {
        log.info("User {} is creating a new item", user.getUsername());
        log.debug("Item details: {}", item);
        log.warn("Received images: {}", images);
        ItemDto createdItem = itemService.create(item, user, images);
        log.info("Item created with id {}", createdItem.getId());
        return createdItem;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId,
                       @AuthenticationPrincipal User user) {
        log.info("User {} requested deletion of item with id {}", user.getUsername(), itemId);
        itemService.delete(itemId, user);
        log.info("Item with id {} has been deleted", itemId);
    }
}
