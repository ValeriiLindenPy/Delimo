package rs.delimo.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.delimo.user.dto.UserDto;

public interface UserService extends UserDetailsService {
    UserDto getById(Long id);

    UserDto editById(Long id, UserDto userDto);

    void deleteById(Long id);

    UserDto getByUserAuth(User user);
}
