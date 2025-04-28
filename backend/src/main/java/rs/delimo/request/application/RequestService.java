package rs.delimo.request.application;

import rs.delimo.api.dto.RequestFilterDto;
import rs.delimo.api.dto.RequestInputDto;
import rs.delimo.api.dto.RequestOutputDto;
import rs.delimo.api.dto.RequestPageResponse;
import rs.delimo.user.domain.User;

import java.util.UUID;


public interface RequestService {
    RequestOutputDto create(RequestInputDto request, User user);

    RequestOutputDto edit(UUID requestID, RequestInputDto request, User user);

    RequestOutputDto getById(UUID requestId);

    void delete(UUID requestID, User user);

    RequestPageResponse getAllByOwner(int page, int pageSize, User user);

    RequestOutputDto getByUserAndId(UUID id, User user);

    RequestPageResponse search(Integer page, Integer size, RequestFilterDto requestFilterDto);
}
