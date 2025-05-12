package rs.delimo.item.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rs.delimo.api.dto.ItemDto;
import rs.delimo.api.dto.ItemRequestDto;
import rs.delimo.api.dto.ItemTitle;
import rs.delimo.common.mapper.VoMapper;
import rs.delimo.item.domain.Item;


@Mapper(componentModel = "spring", uses = VoMapper.class)
public interface ItemMapper {
    @Mapping(source = "id.value", target = "id")
    ItemDto toDto(Item item);

    @Mapping(source = "id.value", target = "id")
    ItemTitle toItemTitle(Item item);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item toEntity(ItemRequestDto dto);
}
