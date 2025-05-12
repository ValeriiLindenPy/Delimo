package rs.delimo.item.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rs.delimo.common.client.ItemClient;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.infrastructure.repository.ItemRepository;

@Component
@RequiredArgsConstructor
public class ItemClientImpl implements ItemClient {
    private final ItemRepository repository;

    @Override
    public void deleteByOwner(UserId id) {
        repository.deleteByOwner(id);
    }
}
