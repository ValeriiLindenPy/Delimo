package rs.delimo.item;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import rs.delimo.item.dto.ItemDto;

import java.util.LinkedHashMap;
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
}
