package rs.delimo.item.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rs.delimo.api.dto.ItemDto;
import rs.delimo.api.dto.ItemRequestDto;
import rs.delimo.api.dto.ItemTitle;
import rs.delimo.item.domain.Item;
import rs.delimo.user.infrastructure.mapper.UserMapper;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ItemMapper {
    ItemDto toDto(Item item);

    ItemTitle toItemTitle(Item item);


    @Mapping(target = "created", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item toEntity(ItemRequestDto dto);
}
