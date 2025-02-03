package rs.delimo.item;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import rs.delimo.item.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rs.delimo.item.dto.ItemTitle;

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
                                      @RequestParam(defaultValue = "6") int pageSize,
                                      @RequestParam(required = false) String city) {
        Page<ItemDto> items = itemService.getAll(city, page, pageSize);
        return createPagedResponse(items);
    }

    @GetMapping("/titles")
    public List<ItemTitle> getTitles(@RequestParam(required = false, defaultValue = "") String q,
                                     @RequestParam(required = false, defaultValue = "5") int limit) {
        return itemService.searchTitles(q, limit);
    }

    @GetMapping("/{id}")
    public ItemDto getOne(@PathVariable Long id) {
        return itemService.getById(id);
    }

    @GetMapping("/search")
    public Map<String, Object> searchAllByText(@RequestParam("text") String text,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "6") int pageSize,
                                               @RequestParam(required = false) String city) {
        Page<ItemDto> items = (text == null || text.isBlank())
                ? itemService.getAll(city, page, pageSize)
                : itemService.searchByText(text, page, pageSize, city);
        return createPagedResponse(items);
    }

    private Map<String, Object> createPagedResponse(Page<ItemDto> items) {
        PagedModel<EntityModel<ItemDto>> model = assembler.toModel(items);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", model.getMetadata());
        response.put("content", model.getContent().stream()
                .map(EntityModel::getContent)
                .toList());
        return response;
    }
}
