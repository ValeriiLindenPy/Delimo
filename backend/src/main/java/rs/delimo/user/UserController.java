package rs.delimo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.error.ValidationMarker;
import rs.delimo.user.dto.UserDto;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user-data")
    public UserDto getUserData(@AuthenticationPrincipal OidcUser user) {
        return userService.getByOidc(user);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PatchMapping("/{id}")
    public UserDto editById(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.editById(id, userDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Validated(ValidationMarker.OnCreate.class)
                              @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
         userService.deleteById(id);
    }
}
