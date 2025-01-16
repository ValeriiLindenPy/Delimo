package rs.delimo.item.dto;

import lombok.Builder;
import lombok.Data;
import rs.delimo.user.dto.UserDto;

import java.util.List;


@Data
@Builder
public class ItemRequestDto {
    private Long id;

    private String title;

    private String description;

    private Integer maxPeriodDays;

    private Integer pricePerDay;

    private String street;

    private String city;

    private String phone;

    private String viber;

}
