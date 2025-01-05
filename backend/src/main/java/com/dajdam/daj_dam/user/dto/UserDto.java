package com.dajdam.daj_dam.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import com.dajdam.daj_dam.error.ValidationMarker;

@Data
@Builder
public class UserDto {
    @Null(groups = ValidationMarker.OnCreate.class, message = "Id should be null")
    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Name can't be blank")
    private String name;

    @Email(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class},
            message = "Wrong email format")
    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Email can't be blank")
    private String email;
}
