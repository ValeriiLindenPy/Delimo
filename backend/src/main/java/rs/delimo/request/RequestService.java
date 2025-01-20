package rs.delimo.request;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.request.dto.RequestInputDto;
import rs.delimo.request.dto.RequestOutputDto;

public interface RequestService {
    Page<RequestOutputDto> getAll(int page, int pageSize);

    RequestOutputDto create(RequestInputDto request, OidcUser user);

    RequestOutputDto edit(Long requestID, RequestInputDto request, OidcUser user);

    RequestOutputDto getById(Long requestId);

    void delete(Long requestID, OidcUser user);

    Page<RequestOutputDto> getAllByOwner(int page, int pageSize, OidcUser user);

    RequestOutputDto getByUserAndId(Long id, OidcUser user);
}
