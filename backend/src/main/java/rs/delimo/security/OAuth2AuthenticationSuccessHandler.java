package rs.delimo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("email");

        // Получаем или создаём пользователя в БД
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalStateException("User not found"));

        // Генерируем JWT токен
        String token = jwtService.generateToken(user);

        // Отправляем редирект с токеном
        response.sendRedirect("http://localhost:5173/?token=" + token);
    }
}
