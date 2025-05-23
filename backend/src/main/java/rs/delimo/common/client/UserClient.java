package rs.delimo.common.client;

import rs.delimo.api.dto.*;
import rs.delimo.common.valueobject.UserId;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface UserClient {

    Map<UserId, UserDto> findByIds(Collection<UserId> userIds);

    UserDto findById(UserId id);

    UserDto findByEmail(String email);

    Set<UserId> findIdsByCity(String city);

    UserDto updateUserContactInfoByRequestUpdate(RequestUpdateDto request, UserDto requester);

    UserDto updateUserContactInfoByRequestCreate(RequestInputDto request, UserDto requester);

    UserDto updateUserContactInfoByItemUpdate(ItemUpdateDto item, UserDto owner);

    UserDto updateUserContactInfoByItemCreate(ItemRequestDto item, UserDto owner);
}
