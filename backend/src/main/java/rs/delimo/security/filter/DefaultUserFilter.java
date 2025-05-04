package rs.delimo.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.delimo.security.service.JwtService;
import rs.delimo.user.domain.Role;
import rs.delimo.user.domain.User;
import rs.delimo.user.infrastructure.repository.UserRepository;


import java.io.IOException;
import java.util.Optional;

@Component("defaultUserFilter")
@Profile("test")
@RequiredArgsConstructor
@Slf4j
public class DefaultUserFilter implements Filter, CommandLineRunner {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public static final String DEFAULT_EMAIL = "default@mail.com";
    public static final String DEFAULT_NAME = "default_user";
    public static final String DEFAULT_PASSWORD = "password";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> optionalUser = repository.findByEmail(DEFAULT_EMAIL);
            User defaultUser = optionalUser.orElseGet(() -> {
                User user = User.builder()
                        .email(DEFAULT_EMAIL)
                        .name(DEFAULT_NAME)
                        .password(passwordEncoder.encode("password"))
                        .role(Role.USER)
                        .enabled(true)
                        .build();
                return user;
            });

            String jwtToken = jwtService.generateToken(defaultUser);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(defaultUser, null, defaultUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setHeader("Authorization", "Bearer " + jwtToken);

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> existUser = repository.findByEmail(DEFAULT_EMAIL);
        if (existUser.isEmpty()) {
            User defaultUser = User.builder()
                    .email(DEFAULT_EMAIL)
                    .name(DEFAULT_NAME)
                    .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            repository.save(defaultUser);
            log.info("Default user created: " + DEFAULT_EMAIL);
        } else {
            log.warn("Default user already exists: " + DEFAULT_EMAIL);
        }
    }
}
