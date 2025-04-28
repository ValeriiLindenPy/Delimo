package rs.delimo.common.mapper;

import org.mapstruct.Mapper;
import rs.delimo.common.valueobject.ItemId;
import rs.delimo.common.valueobject.RequestId;
import rs.delimo.common.valueobject.UserId;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VoMapper {
    RequestId toRequestId(UUID id);
    UUID toUuid(RequestId id);
    ItemId toItemId(UUID id);
    UUID toUuid(ItemId id);
    UserId toUserId(UUID id);
    UUID toUuid(UserId id);
}
