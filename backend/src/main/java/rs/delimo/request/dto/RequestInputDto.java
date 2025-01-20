package rs.delimo.request.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestInputDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Integer pricePerDay;
    @NotBlank
    private String city;
    @NotBlank
    private String phone;

    private String viber;
}
