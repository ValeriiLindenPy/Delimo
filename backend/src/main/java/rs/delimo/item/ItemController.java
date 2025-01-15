package rs.delimo.item;

import rs.delimo.error.ValidationMarker;
import rs.delimo.item.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getOne(@PathVariable Long id) {
        return itemService.getById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAllByText(@RequestParam("text") String text) {
        return itemService.searchByText(text);
    }

    @PatchMapping("/{id}")
    public ItemDto editOne(@PathVariable Long id,
                          @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.editOne(id, item, userId);
    }

    @PostMapping
    public ItemDto create(@Validated(ValidationMarker.OnCreate.class)
                          @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(item, userId);
    }
}
