package rs.delimo.security.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Builder
public class AuthUserDetails implements UserDetails {
    private final UUID id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    @Override
    public String getUsername() { return email; }
    @Override
    public String getPassword() { return null; }
    @Override
    public boolean isEnabled()  { return enabled; }
}
