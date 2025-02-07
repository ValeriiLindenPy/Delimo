package rs.delimo.auth;

import io.jsonwebtoken.Claims;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.delimo.auth.dto.AuthenticationRequest;
import rs.delimo.auth.dto.RegisterRequest;
import rs.delimo.auth.dto.AuthenticationResponse;
import rs.delimo.email.EmailService;
import rs.delimo.error.exception.DuplicatingEmailException;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.security.JwtService;
import rs.delimo.user.Role;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.util.Optional;

/**
 * Service class that handles user authentication operations including registration,
 * login, email verification, and password reset.
 * <p>
 * This service uses JWT tokens for email verification and password reset, and it integrates
 * with the user repository, email service, and security components.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    /**
     * Registers a new user with the provided registration details.
     * <p>
     * The method first checks if a user with the provided email already exists. If not,
     * it creates a new user with the role USER and disabled status. After saving the user,
     * a verification token is generated and sent via email.
     * </p>
     *
     * @param request the registration request containing the new user's details.
     * @return a confirmation message instructing the user to verify their email.
     * @throws DuplicatingEmailException if a user with the given email already exists.
     */
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("User with email {} already exists", request.getEmail());
            throw new DuplicatingEmailException("User with email - %s is already exist".formatted(request.getEmail()));
        }

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

    /**
     * Authenticates a user using the provided credentials.
     * <p>
     * If the credentials are valid, a JWT token is generated and returned as part of the
     * authentication response.
     * </p>
     *
     * @param request the authentication request containing the user's email and password.
     * @return an {@link AuthenticationResponse} containing the generated JWT token.
     * @throws NotFoundException if the user with the provided email is not found.
     */
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

    /**
     * Verifies a user's email using the provided verification token.
     * <p>
     * The method validates the token, ensures it is marked as a verification token, and then
     * retrieves the associated user. If the user is found and not already enabled, their account
     * is activated.
     * </p>
     *
     * @param token the JWT verification token.
     * @throws RuntimeException      if the token is invalid, expired, or not a verification token,
     *                               or if the user is not found.
     * @throws ValidationException   if the user has already been verified.
     */
    public void verifyUser(String token) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Claims claims = jwtService.getDataFromToken(token);

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

    /**
     * Initiates the password reset process for a user identified by their email.
     * <p>
     * A password reset token is generated and sent to the user's email address.
     * </p>
     *
     * @param email the email address of the user requesting a password reset.
     * @return a message indicating that a password reset link has been sent.
     * @throws NotFoundException if the user with the specified email is not found.
     */
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String resetToken = jwtService.generateResetToken(user);
        emailService.sendResetPasswordEmail(user.getEmail(), resetToken);

        return "Password reset link has been sent to your email.";
    }

    /**
     * Resets a user's password using the provided reset token and new password.
     * <p>
     * The method validates the reset token, checks if it is allowed for password reset,
     * and then updates the user's password.
     * </p>
     *
     * @param token       the JWT reset token.
     * @param newPassword the new password to set for the user.
     * @return a message indicating that the password has been reset successfully.
     * @throws RuntimeException if the token is invalid, expired, or not authorized for a password reset,
     *                          or if the user is not found.
     */
    public String resetPassword(String token, String newPassword) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        Claims claims = jwtService.getDataFromToken(token);

        boolean reset = Optional.ofNullable(claims.get("reset", Boolean.class)).orElse(false);
        boolean auth = Optional.ofNullable(claims.get("auth", Boolean.class)).orElse(false);

        if (!reset && !auth) {
            throw new RuntimeException("Token does not allow password reset");
        }

        String email = claims.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password has been reset successfully.";
    }
}
