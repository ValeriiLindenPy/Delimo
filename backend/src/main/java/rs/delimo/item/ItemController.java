package rs.delimo.item;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import rs.delimo.item.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
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

        PagedModel<EntityModel<ItemDto>> pagedModel = assembler.toModel(items);

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
    public Map<String, Object> searchAllByText(@RequestParam("text") String text,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "6") int pageSize) {
        Page<ItemDto> items = itemService.searchByText(text, page, pageSize);

        PagedModel<EntityModel<ItemDto>> pagedModel = assembler.toModel(items);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", pagedModel.getMetadata());
        response.put("content", pagedModel.getContent().stream()
                .map(EntityModel::getContent)
                .toList());
        return response;
    }
}
