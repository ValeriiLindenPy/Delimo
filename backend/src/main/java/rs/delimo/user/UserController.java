package rs.delimo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.delimo.user.dto.UserDto;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user-data")
    public UserDto getUserData(@AuthenticationPrincipal User currentUser) {
        return userService.getByUserAuth(currentUser);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PatchMapping("/{id}")
    public UserDto editById(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.editById(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
         userService.deleteById(id);
    }
}
