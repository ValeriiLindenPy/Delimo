package rs.delimo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.delimo.user.Role;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Component("defaultUserFilter")
@Profile("test")
@RequiredArgsConstructor
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
            // Попытка найти пользователя по умолчанию в базе
            Optional<User> optionalUser = repository.findByEmail(DEFAULT_EMAIL);
            User defaultUser = optionalUser.orElseGet(() -> {
                User user = User.builder()
                        .email(DEFAULT_EMAIL)
                        .name(DEFAULT_NAME)
                        .password(passwordEncoder.encode("password"))  // произвольный пароль
                        .role(Role.USER)
                        .enabled(true)
                        .build();
                // Если пользователь не найден, можно его создать «на лету»
                // Однако CommandLineRunner уже позаботится о создании в базе при старте
                return user;
            });

            // Генерация JWT-токена для пользователя по умолчанию
            String jwtToken = jwtService.generateToken(defaultUser);

            // Создаем объект аутентификации и устанавливаем в контекст
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(defaultUser, null, defaultUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Добавляем JWT в заголовок ответа, чтобы фронтенд мог его использовать
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
            System.out.println("Default user created: " + DEFAULT_EMAIL);
        } else {
            System.out.println("Default user already exists: " + DEFAULT_EMAIL);
        }
    }
}
