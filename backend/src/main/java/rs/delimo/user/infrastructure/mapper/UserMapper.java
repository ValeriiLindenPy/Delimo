package rs.delimo.user.infrastructure.mapper;

import org.mapstruct.Mapper;
import rs.delimo.api.dto.UserDto;
import rs.delimo.user.domain.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
