package rs.delimo.request.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rs.delimo.common.client.RequestClient;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.request.infrastructure.repository.RequestRepository;

@Component
@RequiredArgsConstructor
public class RequestClientImpl implements RequestClient {
    private final RequestRepository repository;

    @Override
    public void deleteByRequester(UserId id) {
        repository.deleteByRequester(id);
    }
}
