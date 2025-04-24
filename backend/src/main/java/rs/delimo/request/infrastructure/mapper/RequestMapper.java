package rs.delimo.request.infrastructure.mapper;


import org.mapstruct.Mapper;
import rs.delimo.api.dto.RequestOutputDto;
import rs.delimo.request.domain.ItemRequest;
import rs.delimo.user.infrastructure.mapper.UserMapper;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RequestMapper {
    RequestOutputDto toOutputDto(ItemRequest request);
}
