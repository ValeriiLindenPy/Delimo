package rs.delimo.auth;

import io.jsonwebtoken.Claims;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.delimo.auth.dto.AuthenticationRequest;
import rs.delimo.auth.dto.RegisterRequest;
import rs.delimo.auth.dto.AuthenticationResponse;
import rs.delimo.email.EmailService;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.security.JwtService;
import rs.delimo.user.Role;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public String register(RegisterRequest request) {
        User newUser = User.builder()
                .role(Role.USER)
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .enabled(false)
                .city(request.getCity())
                .street(request.getStreet())
                .viber(request.getViber())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();
        userRepository.save(newUser);

        String verificationToken = jwtService.generateVerificationToken(newUser);

        emailService.sendVerificationEmail(newUser.getEmail(), verificationToken);

        return "Please go to email box to verify your email";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public void verifyUser(String token) {

        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Claims claims = jwtService.getUsernameFromToken(token);

        Boolean isVerificationToken = claims.get("verification", Boolean.class);
        if (isVerificationToken == null || !isVerificationToken) {
            throw new RuntimeException("Not a verification token");
        }

        String email = claims.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEnabled()) {
            throw new ValidationException("User has already verified");
        }

        user.setEnabled(true);

        userRepository.save(user);
    }
}
