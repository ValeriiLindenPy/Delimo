package rs.delimo.item;

import rs.delimo.user.UserMapper;
import org.springframework.stereotype.Component;
import rs.delimo.item.dto.ItemDto;

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
                .owner(UserMapper.toUser(dto.getOwner()))
                .available(dto.getAvailable()).build();
    }
}
