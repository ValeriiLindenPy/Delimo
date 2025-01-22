package rs.delimo.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ItemRequestDto {

    private String title;

    private String description;

    private Integer maxPeriodDays;

    private Integer pricePerDay;

    private String street;

    private Boolean available;

    private String city;

    private String phone;

    private String viber;

}
