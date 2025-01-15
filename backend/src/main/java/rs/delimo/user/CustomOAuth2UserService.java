package rs.delimo.user;

import lombok.NoArgsConstructor;
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
        log.info("start log");
        OidcUser oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        log.info("email {}", email);
        String name = oAuth2User.getAttribute("name");
        log.info("name {}", email);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = User.builder()
                            .name(name)
                            .email(email)
                            .build();
                    return userRepository.save(newUser);
                }
        );

        log.info("useriD: {}", user.getId());
        log.info("user created");


        return new DefaultOidcUser(
                oAuth2User.getAuthorities(),
                userRequest.getIdToken(),
                "email"
        );
    }
}
