package com.dajdam.daj_dam.item;

import com.dajdam.daj_dam.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long itemId);

    ItemDto editOne(Long id, ItemDto item, Long userId);

    List<ItemDto> searchByText(String text);

    ItemDto create(ItemDto item, Long userId);

}
