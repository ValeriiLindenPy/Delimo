package rs.delimo.item;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final PagedResourcesAssembler<ItemDto> assembler;

    @GetMapping
    public Map<String, Object> getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int pageSize,
                                      @RequestParam(required = false) String city) {
        log.info("Request to get all items: page={}, pageSize={}, city={}", page, pageSize, city);
        Page<ItemDto> items = itemService.getAll(city, page, pageSize);
        log.info("Found {} items", items.getTotalElements());
        Map<String, Object> response = createPagedResponse(items);
        log.debug("Response: {}", response);
        return response;
    }

    @GetMapping("/titles")
    public List<ItemTitle> getTitles(@RequestParam(required = false, defaultValue = "") String q,
                                     @RequestParam(required = false, defaultValue = "5") int limit) {
        log.info("Request to search item titles: q='{}', limit={}", q, limit);
        List<ItemTitle> titles = itemService.searchTitles(q, limit);
        log.info("Found {} titles", titles.size());
        return titles;
    }

    @GetMapping("/{id}")
    public ItemDto getOne(@PathVariable Long id) {
        log.info("Request to get item by id: {}", id);
        ItemDto item = itemService.getById(id);
        log.info("Retrieved item: {}", item);
        return item;
    }

    @GetMapping("/search")
    public Map<String, Object> searchAllByText(@RequestParam("text") String text,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "6") int pageSize,
                                               @RequestParam(required = false) String city) {
        log.info("Request to search items by text: text='{}', page={}, pageSize={}, city={}", text, page, pageSize, city);
        Page<ItemDto> items;
        if (text == null || text.isBlank()) {
            log.debug("Empty search text provided, retrieving all items");
            items = itemService.getAll(city, page, pageSize);
        } else {
            items = itemService.searchByText(text, page, pageSize, city);
        }
        log.info("Found {} items for text '{}'", items.getTotalElements(), text);
        Map<String, Object> response = createPagedResponse(items);
        log.debug("Search response: {}", response);
        return response;
    }

    private Map<String, Object> createPagedResponse(Page<ItemDto> items) {
        log.debug("Creating paged response for {} items", items.getTotalElements());
        PagedModel<EntityModel<ItemDto>> model = assembler.toModel(items);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", model.getMetadata());
        response.put("content", model.getContent().stream()
                .map(EntityModel::getContent)
                .toList());
        log.debug("Paged response created: {}", response);
        return response;
    }
}
