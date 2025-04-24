package rs.delimo.user.application;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.delimo.api.dto.UserDto;
import rs.delimo.user.domain.User;

import java.util.UUID;


public interface UserService extends UserDetailsService {
    UserDto getById(UUID id);

    UserDto editById(UUID id, UserDto userDto);

    void deleteById(UUID id);

    UserDto getByUserAuth(User user);
}
