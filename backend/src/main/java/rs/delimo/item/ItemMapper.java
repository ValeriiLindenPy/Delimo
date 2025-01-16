package rs.delimo.item;

import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.user.UserMapper;
import org.springframework.stereotype.Component;
import rs.delimo.item.dto.ItemDto;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .available(item.getAvailable())
                .image(item.getImage())
                .maxPeriodDays(item.getMaxPeriodDays())
                .pricePerDay(item.getPricePerDay())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .build();
    }

    public static Item toItem(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .owner(UserMapper.toUser(dto.getOwner()))
                .available(dto.getAvailable())
                .maxPeriodDays(dto.getMaxPeriodDays())
                .pricePerDay(dto.getPricePerDay())
                .image(dto.getImage())
                .build();
    }

    public static Item toItem(ItemRequestDto dto) {
        return Item.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .maxPeriodDays(dto.getMaxPeriodDays())
                .pricePerDay(dto.getPricePerDay())
                .build();
    }
}
