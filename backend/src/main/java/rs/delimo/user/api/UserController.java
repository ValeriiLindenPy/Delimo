package rs.delimo.user.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.UsersApi;
import rs.delimo.api.dto.*;
import rs.delimo.user.application.AuthenticationService;
import rs.delimo.user.application.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UsersApi {
    private final UserService service;
    private final AuthenticationService authService;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Received authentication request for email: {}", authenticationRequest.getEmail());
        AuthenticationResponse response = authService.authenticate(authenticationRequest);
        log.info("Authentication successful for email: {}", authenticationRequest.getEmail());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Received forgot password request for email: {}", forgotPasswordRequest.getEmail());
        String response = authService.forgotPassword(forgotPasswordRequest.getEmail());
        log.info("Processed forgot password request for email: {}", forgotPasswordRequest.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest registerRequest) {
        log.info("Received registration request for email: {}", registerRequest.getEmail());
        authService.register(registerRequest);
        log.info("Registration completed for email: {}", registerRequest.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        log.info("Received reset password request with token: {}", resetPasswordRequest.getToken());
        String response = authService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
        log.info("Password reset completed for token: {}", resetPasswordRequest.getToken());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> verifyUser(String token) {
        log.info("Received request to verify user with token: {}", token);
        authService.verifyUser(token);
        log.info("User verified successfully for token: {}", token);
        return ResponseEntity.ok("User verified successfully!");
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDto> getUser(UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<Void> updateUser(UUID id, UserDto userDto) {
        service.editById(id, userDto);
        return ResponseEntity.noContent().build();
    }
}
