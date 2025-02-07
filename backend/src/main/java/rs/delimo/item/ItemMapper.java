package rs.delimo.item;

import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.item.dto.ItemTitle;
import rs.delimo.user.UserMapper;
import org.springframework.stereotype.Component;
import rs.delimo.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .created(item.getCreated())
                .title(item.getTitle())
                .description(item.getDescription())
                .available(item.getAvailable())
                .images(item.getImages() != null ? new ArrayList<>(item.getImages()) : Collections.emptyList())
                .maxPeriodDays(item.getMaxPeriodDays())
                .pricePerDay(item.getPricePerDay())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .build();
    }

    public static ItemTitle toItemTitle(Item item) {
        return ItemTitle.builder()
                .id(item.getId())
                .title(item.getTitle())
                .build();
    }

    public static Item toItem(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .owner(UserMapper.toUser(dto.getOwner()))
                .available(dto.getAvailable())
                .maxPeriodDays(dto.getMaxPeriodDays())
                .pricePerDay(dto.getPricePerDay())
                .images(dto.getImages())
                .build();
    }

    public static Item toItem(ItemRequestDto dto) {
        return Item.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .maxPeriodDays(dto.getMaxPeriodDays())
                .pricePerDay(dto.getPricePerDay())
                .available(dto.getAvailable())
                .build();
    }
}
