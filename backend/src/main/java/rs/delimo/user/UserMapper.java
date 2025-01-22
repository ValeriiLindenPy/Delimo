package rs.delimo.user;

import org.springframework.stereotype.Component;
import rs.delimo.user.dto.UserDto;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .confirmed(user.getConfirmed())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .viber(user.getViber())
                .city(user.getCity())
                .street(user.getStreet())
                .build();
    }

    public static User toUser(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .city(dto.getCity())
                .street(dto.getStreet())
                .confirmed(dto.getConfirmed())
                .viber(dto.getViber())
                .phone(dto.getPhone())
                .build();
    }
}
