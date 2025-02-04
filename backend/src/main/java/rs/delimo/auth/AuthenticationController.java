package rs.delimo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.auth.dto.*;
import rs.delimo.email.EmailService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    private final EmailService emailService;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String response = service.forgotPassword(request.getEmail());
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        String response = service.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", response));
    }
}
