package com.dajdam.daj_dam.item;

import org.springframework.stereotype.Component;
import com.dajdam.daj_dam.item.dto.ItemDto;
import com.dajdam.daj_dam.item.model.Item;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable()).build();
    }

    public static Item toItem(ItemDto dto) {
        return Item.builder().id(dto.getId()).name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable()).build();
    }
}
