package rs.delimo.request;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import rs.delimo.request.dto.RequestDto;
import rs.delimo.request.dto.RequestForResponseDto;

public interface RequestService {
    Page<RequestForResponseDto> getAll(int page, int pageSize);

    RequestForResponseDto create(RequestDto request, OidcUser user);

    RequestForResponseDto edit(Long requestID, RequestDto request, OidcUser user);

    RequestForResponseDto getById(Long requestId);

    void delete(Long requestID, OidcUser user);
}
