package rs.delimo.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.auth.dto.*;
import rs.delimo.email.EmailService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService service;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Validated RegisterRequest request) {
        log.info("Received registration request for email: {}", request.getEmail());
        String responseText = service.register(request);
        log.info("Registration completed for email: {}", request.getEmail());
        return ResponseEntity.ok(Map.of("text", responseText));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Validated AuthenticationRequest request) {
        log.info("Received authentication request for email: {}", request.getEmail());
        AuthenticationResponse response = service.authenticate(request);
        log.info("Authentication successful for email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        log.info("Received request to verify user with token: {}", token);
        service.verifyUser(token);
        log.info("User verified successfully for token: {}", token);
        return ResponseEntity.ok("User verified successfully!");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        log.info("Received forgot password request for email: {}", request.getEmail());
        String response = service.forgotPassword(request.getEmail());
        log.info("Processed forgot password request for email: {}", request.getEmail());
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        log.info("Received reset password request with token: {}", request.getToken());
        String response = service.resetPassword(request.getToken(), request.getNewPassword());
        log.info("Password reset completed for token: {}", request.getToken());
        return ResponseEntity.ok(Map.of("message", response));
    }
}
