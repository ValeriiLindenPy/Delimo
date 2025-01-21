package rs.delimo.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.delimo.user.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JwtService {
    private final long jwtExpirationMs = TimeUnit.DAYS.toMillis(30);
    private final SecretKey secretKey;

    public JwtService() {
        Dotenv dotenv = Dotenv.load();
        String secret = dotenv.get("JWT_SECRET");
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        Date expiryDate = Date.from(Instant.now().plusMillis(jwtExpirationMs));

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().toString())
                .claim("userId", user.getId())
                .expiration(expiryDate)
                .issuedAt(Date.from(Instant.now()))
                .signWith(secretKey)
                .compact();
    }


    public Claims getUsernameFromToken(String token) {
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
