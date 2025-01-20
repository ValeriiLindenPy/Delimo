package rs.delimo.request.dto;


import lombok.Builder;
import lombok.Data;
import rs.delimo.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestOutputDto {
    private Long id;

    private String title;

    private String description;

    private Integer pricePerDay;

    private UserDto requester;

    private LocalDateTime created;

}
