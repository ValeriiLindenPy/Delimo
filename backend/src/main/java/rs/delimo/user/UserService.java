package rs.delimo.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import rs.delimo.user.dto.UserDto;

public interface UserService extends UserDetailsService {
    UserDto getById(Long id);

    UserDto editById(Long id, UserDto userDto);

    UserDto create(UserDto userDto);

    void deleteById(Long id);

    UserDto getByOidc(OidcUser user);
}
