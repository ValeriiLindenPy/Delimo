package rs.delimo.request.infrastructure.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.delimo.api.dto.RequestOutputDto;
import rs.delimo.common.mapper.VoMapper;
import rs.delimo.request.domain.ItemRequest;
import rs.delimo.user.infrastructure.mapper.UserMapper;


@Mapper(componentModel = "spring", uses = {UserMapper.class, VoMapper.class})
public interface RequestMapper {
    @Mapping(source = "id.value", target = "id")
    RequestOutputDto toOutputDto(ItemRequest request);
}
