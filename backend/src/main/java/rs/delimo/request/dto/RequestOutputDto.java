package rs.delimo.request.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd-MM-yyyy | HH:mm")
    private LocalDateTime created;

}
