package rs.delimo.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rs.delimo.user.CustomOAuth2UserService;
import rs.delimo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                            auth.requestMatchers("/items").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .oauth2Login(
                        auth ->
                        auth.
                                userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(
                                         CustomOAuth2UserService()
                                ))
                                .successHandler(
                                ((request, response, authentication) ->
                                response.sendRedirect("http://localhost:5173/")))
                )
                .logout(logout -> logout.logoutSuccessUrl("http://localhost:5173/"));


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> CustomOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }


}


