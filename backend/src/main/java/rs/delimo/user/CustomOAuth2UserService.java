package rs.delimo.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends OidcUserService {
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.trace("start log");
        OidcUser oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        log.trace("email {}", email);
        String name = oAuth2User.getAttribute("name");
        log.trace("name {}", email);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = User.builder()
                            .name(name)
                            .email(email)
                            .enabled(true)
                            .role(Role.USER)
                            .build();
                    return userRepository.save(newUser);
                }
        );

        log.trace("useriD: {}", user.getId());
        log.trace("user created");


        return new DefaultOidcUser(
                oAuth2User.getAuthorities(),
                userRequest.getIdToken(),
                "email"
        );
    }
}
