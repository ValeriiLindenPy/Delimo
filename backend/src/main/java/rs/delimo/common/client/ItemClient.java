package rs.delimo.common.client;

import rs.delimo.common.valueobject.UserId;

public interface ItemClient {
    void deleteByOwner(UserId id);
}
