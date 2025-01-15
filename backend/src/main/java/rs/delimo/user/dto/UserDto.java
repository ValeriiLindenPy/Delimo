package rs.delimo.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import rs.delimo.error.ValidationMarker;

@Data
@Builder
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String city;

    private String address;

    private String telephone;

    private String viber;
}
