package rs.delimo.request;

import org.springframework.data.domain.Page;
import rs.delimo.request.dto.RequestInputDto;
import rs.delimo.request.dto.RequestOutputDto;
import rs.delimo.user.User;

public interface RequestService {
    Page<RequestOutputDto> getAll(int page, int pageSize);

    Page<RequestOutputDto> getAll(String city, int page, int pageSize);

    RequestOutputDto create(RequestInputDto request, User user);

    RequestOutputDto edit(Long requestID, RequestInputDto request, User user);

    RequestOutputDto getById(Long requestId);

    void delete(Long requestID, User user);

    Page<RequestOutputDto> getAllByOwner(int page, int pageSize, User user);

    RequestOutputDto getByUserAndId(Long id, User user);

    Page<RequestOutputDto> search(String city, String text, int page, int pageSize);
}
