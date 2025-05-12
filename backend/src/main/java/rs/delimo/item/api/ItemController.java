package rs.delimo.item.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.ItemsApi;
import rs.delimo.api.dto.ItemDto;
import rs.delimo.api.dto.ItemFilterDto;
import rs.delimo.api.dto.ItemPageResponse;
import rs.delimo.api.dto.ItemTitle;
import rs.delimo.item.application.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController implements ItemsApi {
    private final ItemService service;

    @Override
    public ResponseEntity<ItemDto> getItem(UUID id) {
        log.debug("Getting item with ID: {}", id);
        ItemDto item = service.getById(id);
        log.debug("Retrieved item: {}", item);
        return ResponseEntity.ok(item);
    }

    @Override
    public ResponseEntity<List<ItemTitle>> listItemTitles(String q, Integer limit) {
        log.debug("Searching item titles with query='{}', limit={}", q, limit);
        List<ItemTitle> titles = service.searchTitles(q, limit);
        log.debug("Found {} titles", titles.size());
        return ResponseEntity.ok(titles);
    }

    @Override
    public ResponseEntity<ItemPageResponse> listItems(Integer page, Integer size) {
        log.debug("Listing items with page={}, size={}", page, size);
        ItemPageResponse response = service.listItems(page, size);
        log.debug("Item page response: {}", response);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ItemPageResponse> searchItems(Integer page, Integer pageSize, ItemFilterDto itemFilterDto) {
        log.debug("Searching items with page={}, pageSize={}, filters={}", page, pageSize, itemFilterDto);
        ItemPageResponse response = service.search(page, pageSize, itemFilterDto);
        log.debug("Search result: {}", response);
        return ResponseEntity.ok(response);
    }
}
