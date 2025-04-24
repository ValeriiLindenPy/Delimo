package rs.delimo.request.application;

import org.springframework.data.domain.Page;
import rs.delimo.api.dto.RequestInputDto;
import rs.delimo.api.dto.RequestOutputDto;
import rs.delimo.user.domain.User;

import java.util.UUID;


public interface RequestService {
    Page<RequestOutputDto> getAll(int page, int pageSize);

    Page<RequestOutputDto> getAll(String city, int page, int pageSize);

    RequestOutputDto create(RequestInputDto request, User user);

    RequestOutputDto edit(UUID requestID, RequestInputDto request, User user);

    RequestOutputDto getById(UUID requestId);

    void delete(UUID requestID, User user);

    Page<RequestOutputDto> getAllByOwner(int page, int pageSize, User user);

    RequestOutputDto getByUserAndId(UUID id, User user);

    Page<RequestOutputDto> search(String city, String text, int page, int pageSize);
}
