package rs.delimo.request.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import rs.delimo.error.ValidationMarker;

@Data
@Builder
public class RequestInputDto {
    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Title shouldn't be blank")
    private String title;
    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Description shouldn't be blank")
    private String description;
    @NotNull(groups = ValidationMarker.OnCreate.class, message = "Price shouldn't be null")
    private Integer pricePerDay;
    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "City shouldn't be blank")
    private String city;
    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Phone shouldn't be blank")
    private String phone;

    private String viber;
}
