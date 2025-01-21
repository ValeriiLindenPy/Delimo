package rs.delimo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.delimo.auth.dto.AuthenticationRequest;
import rs.delimo.auth.dto.RegisterRequest;
import rs.delimo.auth.dto.AuthenticationResponse;
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

    public AuthenticationResponse register(RegisterRequest request) {
        User newUser = User.builder()
                .role(Role.USER)
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .enabled(true)
                .city(request.getCity())
                .street(request.getStreet())
                .viber(request.getViber())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();
        userRepository.save(newUser);
        String token = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
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
}
