package rs.delimo.request;

import rs.delimo.request.dto.RequestForResponseDto;
import rs.delimo.user.UserMapper;

public class RequestMapper {

    public static RequestForResponseDto toResponseDto(ItemRequest request) {
        return RequestForResponseDto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .requester(UserMapper.toUserDto(request.getRequester()))
                .description(request.getDescription())
                .created(request.getCreated())
                .pricePerDay(request.getPricePerDay())
                .build();
    }
}
