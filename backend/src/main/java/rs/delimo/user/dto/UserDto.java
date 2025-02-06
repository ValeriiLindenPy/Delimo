package rs.delimo.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    private String street;

    private String phone;

    private Boolean enabled;

    private String viber;
}
