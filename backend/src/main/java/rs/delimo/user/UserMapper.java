package rs.delimo.user;

import org.springframework.stereotype.Component;
import rs.delimo.user.dto.UserDto;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder().id(user.getId()).name(user.getName())
                .email(user.getEmail()).build();
    }

    public static User toUser(UserDto dto) {
        return User.builder().id(dto.getId()).name(dto.getName())
                .email(dto.getEmail()).build();
    }
}
