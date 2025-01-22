package rs.delimo.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.delimo.error.CustomAccessDeniedHandler;
import rs.delimo.error.CustomAuthenticationEntryPoint;
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
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtService jwtService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers("/items/**").permitAll();
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers("/requests/**").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/my-items/**").authenticated();
                    auth.anyRequest().authenticated();
                })

                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(CustomOAuth2UserService()))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                )

                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionHandling.accessDeniedHandler(accessDeniedHandler);
                })

                .addFilterBefore(
                        new JWTAuthenticationFilter(jwtService, userRepository),
                        UsernamePasswordAuthenticationFilter.class
                )

                .logout(logout -> logout.logoutSuccessUrl("http://localhost:5173/"));

        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> CustomOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


