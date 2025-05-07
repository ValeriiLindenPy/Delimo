package rs.delimo.item.api;

import lombok.RequiredArgsConstructor;
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
public class ItemController implements ItemsApi {
    private final ItemService service;

    @Override
    public ResponseEntity<ItemDto> getItem(UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<List<ItemTitle>> listItemTitles(String q, Integer limit) {
        return ResponseEntity.ok(service.searchTitles(q, limit));
    }

    @Override
    public ResponseEntity<ItemPageResponse> listItems(Integer page, Integer size) {
        return ResponseEntity.ok(service.listItems(page, size));
    }

    @Override
    public ResponseEntity<ItemPageResponse> searchItems(Integer page, Integer pageSize, ItemFilterDto itemFilterDto) {
        return ResponseEntity.ok(service.search(page, pageSize, itemFilterDto));
    }
}
