package rs.delimo.request.application;

import rs.delimo.api.dto.*;
import rs.delimo.common.valueobject.UserId;
import java.util.UUID;


public interface RequestService {
    RequestOutputDto create(RequestInputDto request, UserId userId);

    RequestOutputDto edit(UUID requestID, RequestUpdateDto request, UserId userId);

    RequestOutputDto getById(UUID requestId);

    void delete(UUID requestID, UserId userId);

    RequestPageResponse getAllByOwner(int page, int pageSize, UserId userId);

    RequestOutputDto getByUserAndId(UUID id, UserId userId);

    RequestPageResponse search(Integer page, Integer size, RequestFilterDto requestFilterDto);

    RequestPageResponse listRequests(Integer page, Integer size);
}
