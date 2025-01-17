package rs.delimo.item;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.ValidationMarker;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/my-items")
@RequiredArgsConstructor
public class MyItemController {
    public final ItemService itemService;
    private final PagedResourcesAssembler<ItemDto> assembler;


    @GetMapping
    public Map<String, Object> getAll(@AuthenticationPrincipal OidcUser user,
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
    public ItemDto getOne(@PathVariable Long id, @AuthenticationPrincipal OidcUser user) {
        return itemService.getByUserAndId(id, user);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editOne(@ModelAttribute @Validated(ValidationMarker.OnCreate.class) ItemRequestDto item,
                           @RequestParam(value = "image", required = false) List<MultipartFile> images,
                           @AuthenticationPrincipal OidcUser user,
                           @PathVariable Long itemId
    ) {
        return itemService.editOne(itemId, item, user, images);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ItemDto create(@ModelAttribute @Validated(ValidationMarker.OnCreate.class) ItemRequestDto item,
                          @RequestParam(value = "image", required = false) List<MultipartFile> images,
                          @AuthenticationPrincipal OidcUser user) {
        return itemService.create(item, user, images);
    }
}
