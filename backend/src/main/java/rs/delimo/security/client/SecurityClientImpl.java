package rs.delimo.security.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rs.delimo.common.client.SecurityClient;
import rs.delimo.security.auth.AuthUserDetails;

import java.util.UUID;

@Component
public class SecurityClientImpl implements SecurityClient {
    @Override
    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthUserDetails details)) {
            throw new IllegalStateException("No authenticated user");
        }
        return details.getId();
    }
}
