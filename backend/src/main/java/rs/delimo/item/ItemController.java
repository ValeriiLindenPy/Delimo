package rs.delimo.item;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.ValidationMarker;
import rs.delimo.item.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.item.dto.ItemRequestDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public final ItemService itemService;
    private final PagedResourcesAssembler<ItemDto> assembler;

    @GetMapping
    public Map<String, Object> getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int pageSize) {
        Page<ItemDto> items = itemService.getAll(page, pageSize);

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
    public ItemDto getOne(@PathVariable Long id) {
        return itemService.getById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAllByText(@RequestParam("text") String text) {
        return itemService.searchByText(text);
    }



}
