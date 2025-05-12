package rs.delimo.common.client;

import rs.delimo.common.valueobject.UserId;

public interface RequestClient {
    void deleteByRequester(UserId id);
}
