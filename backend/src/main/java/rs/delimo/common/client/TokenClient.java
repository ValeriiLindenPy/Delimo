package rs.delimo.common.client;

import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface TokenClient {
    String generateToken(UUID userId, String email, String role);

    String generateVerificationToken(UUID userId, String email);

    boolean validateToken(String token);

    Claims getDataFromToken(String token);

    String resetToken(UUID userId, String email);
}
