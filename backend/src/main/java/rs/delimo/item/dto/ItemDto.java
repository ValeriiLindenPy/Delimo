package rs.delimo.item.dto;

import rs.delimo.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

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
}
