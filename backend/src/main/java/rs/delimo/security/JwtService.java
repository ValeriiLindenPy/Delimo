package rs.delimo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.delimo.user.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JwtService {
    private final long MAX_DAYS_TOKEN_VALID = 30;
    private final long MAX_DAYS_VERIFICATION_TOKEN_VALID = 1;
    private final long MAX_DAYS_RESET_TOKEN_VALID = 1;
    private final long jwtExpirationMs = TimeUnit.DAYS.toMillis(MAX_DAYS_TOKEN_VALID);
    private final long resetTokenExpirationMs = TimeUnit.DAYS.toMillis(MAX_DAYS_RESET_TOKEN_VALID);
    private final long verificationTokenExpirationMs = TimeUnit.DAYS.toMillis(MAX_DAYS_VERIFICATION_TOKEN_VALID);
    private final SecretKey secretKey;

    public JwtService(@Value("${app.jwt.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(User user) {
        Date expiryDate = Date.from(Instant.now().plusMillis(jwtExpirationMs));

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("auth", true)
                .claim("role", user.getRole().toString())
                .claim("userId", user.getId())
                .expiration(expiryDate)
                .issuedAt(Date.from(Instant.now()))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generates a short-lived JWT specifically for email verification.
     */
    public String generateVerificationToken(User user) {
        Date expiryDate = Date.from(Instant.now().plusMillis(verificationTokenExpirationMs));

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("verification", true)
                .claim("userId", user.getId())
                .expiration(expiryDate)
                .issuedAt(Date.from(Instant.now()))
                .signWith(secretKey)
                .compact();
    }

    public String generateResetToken(User user) {
        Date expiryDate = Date.from(Instant.now().plusMillis(resetTokenExpirationMs));

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("reset", true)
                .claim("userId", user.getId())
                .expiration(expiryDate)
                .issuedAt(Date.from(Instant.now()))
                .signWith(secretKey)
                .compact();
    }

    public Claims getDataFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT Token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT Token is malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT Token signature validation failed: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("Invalid JWT Token: {}", e.getMessage());
        }
        return false;
    }
}
