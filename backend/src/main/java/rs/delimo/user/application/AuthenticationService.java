package rs.delimo.user.application;

import rs.delimo.api.dto.AuthenticationRequest;
import rs.delimo.api.dto.AuthenticationResponse;
import rs.delimo.api.dto.RegisterRequest;

public interface AuthenticationService {
    void register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void verifyUser(String token);

    String forgotPassword(String email);

    String resetPassword(String token, String newPassword);
}
