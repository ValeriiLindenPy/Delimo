package com.dajdam.daj_dam.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     *  уникальный идентификатор пользователя
     */
    private Long id;
    /**
     *  имя или логин пользователя
     */
    private String name;

    private String password;

    private String role;
    /**
     * уникальный адрес электронной почты
     */
    private String email;
}
