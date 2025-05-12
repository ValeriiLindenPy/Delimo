package rs.delimo.user.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.UsersApi;
import rs.delimo.api.dto.*;
import rs.delimo.common.client.SecurityClient;
import rs.delimo.user.application.AuthenticationService;
import rs.delimo.user.application.UserService;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController implements UsersApi {
    private final UserService service;
    private final AuthenticationService authService;
    private final SecurityClient securityClient;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        log.debug("Received authentication request for email: {}", authenticationRequest.getEmail());
        AuthenticationResponse response = authService.authenticate(authenticationRequest);
        log.debug("Authentication successful for email: {}", authenticationRequest.getEmail());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        log.debug("Received forgot password request for email: {}", forgotPasswordRequest.getEmail());
        authService.forgotPassword(forgotPasswordRequest.getEmail());
        log.debug("Processed forgot password request for email: {}", forgotPasswordRequest.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest registerRequest) {
        log.debug("Received registration request for email: {}", registerRequest.getEmail());
        authService.register(registerRequest);
        log.debug("Registration completed for email: {}", registerRequest.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        log.debug("Received reset password request with token: {}", resetPasswordRequest.getToken());
        authService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
        log.debug("Password reset completed for token: {}", resetPasswordRequest.getToken());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> verifyUser(String token) {
        log.debug("Received request to verify user with token: {}", token);
        authService.verifyUser(token);
        log.debug("User verified successfully for token: {}", token);
        return ResponseEntity.ok("User verified successfully!");
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDto> getUser(UUID id) {
        log.debug("Received request to get user with id: {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<UserDto> getUserData() {
        return ResponseEntity.ok(service.getById(securityClient.getCurrentUserId()));
    }

    @Override
    public ResponseEntity<Void> updateUser(UUID id, UserDto userDto) {
        service.editById(id, userDto);
        return ResponseEntity.noContent().build();
    }
}
