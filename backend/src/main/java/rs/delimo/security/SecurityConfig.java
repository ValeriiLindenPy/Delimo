package rs.delimo.security;

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
                            auth.requestMatchers("/").permitAll();
                            auth.requestMatchers("/public/**").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .oauth2Login(
                        auth ->
                        auth.successHandler(
                                ((request, response, authentication) ->
                                response.sendRedirect("http://localhost:5173/")))
                )
                .logout(logout -> logout.logoutSuccessUrl("http://localhost:5173/"))
                .formLogin(Customizer.withDefaults());

        return http.build();
    }


}


