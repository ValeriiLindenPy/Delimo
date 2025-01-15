package rs.delimo.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import rs.delimo.user.CustomOAuth2UserService;
import rs.delimo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); // Allow OPTIONS requests
                            auth.requestMatchers("/").permitAll();
                            auth.requestMatchers("/public/**").permitAll();
                            auth.requestMatchers("/users/user-data").permitAll();
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
                .logout(logout -> logout.logoutSuccessUrl("http://localhost:5173/"))
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> CustomOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }


}


