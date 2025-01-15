package rs.delimo.item;

import rs.delimo.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long itemId);

    ItemDto editOne(Long id, ItemDto item, Long userId);

    List<ItemDto> searchByText(String text);

    ItemDto create(ItemDto item, Long userId);

}
