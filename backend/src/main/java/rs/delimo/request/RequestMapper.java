package rs.delimo.request;

import rs.delimo.request.dto.RequestOutputDto;
import rs.delimo.user.UserMapper;

public class RequestMapper {

    public static RequestOutputDto toOutputDto(ItemRequest request) {
        return RequestOutputDto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .requester(UserMapper.toUserDto(request.getRequester()))
                .description(request.getDescription())
                .created(request.getCreated())
                .pricePerDay(request.getPricePerDay())
                .build();
    }
}
