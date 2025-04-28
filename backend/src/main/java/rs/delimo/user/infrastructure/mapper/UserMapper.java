package rs.delimo.user.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.delimo.api.dto.UserDto;
import rs.delimo.common.mapper.VoMapper;
import rs.delimo.user.domain.User;


@Mapper(componentModel = "spring", uses = VoMapper.class)
public interface UserMapper {
    @Mapping(source = "id.value", target = "id")
    UserDto toDto(User user);
}
