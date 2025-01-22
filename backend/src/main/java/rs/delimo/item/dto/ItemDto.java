package rs.delimo.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import rs.delimo.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class ItemDto {
    private Long id;

    private String title;

    private String description;

    private Boolean available;

    private Integer maxPeriodDays;

    private Integer pricePerDay;

    private List<String> images;

    private UserDto owner;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
