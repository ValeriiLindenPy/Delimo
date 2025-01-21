package rs.delimo.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    public final ItemService itemService;
    private final PagedResourcesAssembler<ItemDto> assembler;


    @GetMapping
    public Map<String, Object> getAll(@AuthenticationPrincipal User user,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int size) {
        Page<ItemDto> items = itemService.getAllByOwner(page, size, user);

        // Generate PagedModel
        PagedModel<EntityModel<ItemDto>> pagedModel = assembler.toModel(items);

        // Transform the response to replace `_embedded` with `content`
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", pagedModel.getMetadata());
        response.put("content", pagedModel.getContent().stream()
                .map(EntityModel::getContent)
                .toList());
        return response;
    }

    @GetMapping("/{id}")
    public ItemDto getOne(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return itemService.getByUserAndId(id, user);
    }

    @PatchMapping(value = "/{itemId}", consumes = {"multipart/form-data"})
    public ItemDto editOne(
            @ModelAttribute @Validated(ValidationMarker.OnCreate.class) ItemRequestDto item,
            @RequestParam(value = "image", required = false) List<MultipartFile> images,
            @RequestParam(value = "existingImages", required = false) String existingImagesJson,
            @AuthenticationPrincipal User user,
            @PathVariable Long itemId
    ) {
        log.info("Received {} new images", images != null ? images.size() : 0);
        log.info("Existing images JSON: {}", existingImagesJson);
        return itemService.editOne(itemId, item, user, images, existingImagesJson);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ItemDto create(@ModelAttribute @Validated(ValidationMarker.OnCreate.class) ItemRequestDto item,
                          @RequestParam(value = "image", required = false) List<MultipartFile> images,
                          @AuthenticationPrincipal User user) {
        return itemService.create(item, user, images);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    public void delete(
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user) {
         itemService.delete(itemId, user);
    }
}
