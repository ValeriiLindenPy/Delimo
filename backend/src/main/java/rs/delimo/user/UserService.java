package rs.delimo.user;

import rs.delimo.user.dto.UserDto;

public interface UserService {
    UserDto getById(Long id);

    UserDto editById(Long id, UserDto userDto);

    UserDto create(UserDto userDto);

    void deleteById(Long id);
}
