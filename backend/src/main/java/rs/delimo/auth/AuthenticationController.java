package rs.delimo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.auth.dto.AuthenticationRequest;
import rs.delimo.auth.dto.RegisterRequest;
import rs.delimo.auth.dto.AuthenticationResponse;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Validated RegisterRequest request) {
        return ResponseEntity.ok(Map.of("text", service.register(request)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Validated AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        service.verifyUser(token);
        return ResponseEntity.ok("User verified successfully!");
    }
}
