package rs.delimo.common.client;

import rs.delimo.api.dto.RequestInputDto;
import rs.delimo.api.dto.RequestUpdateDto;
import rs.delimo.api.dto.UserDto;
import rs.delimo.common.valueobject.UserId;

import java.util.Collection;
import java.util.Map;

public interface UserClient {

    Map<UserId, UserDto> findByIds(Collection<UserId> userIds);

    UserDto findById(UserId id);

    UserDto findByEmail(String email);

    UserDto updateUserContactInfoByRequestUpdate(RequestUpdateDto request, UserDto requester);

    UserDto updateUserContactInfoByRequestCreate(RequestInputDto request, UserDto requester);
}
