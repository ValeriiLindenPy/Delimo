package com.dajdam.daj_dam.user;

import com.dajdam.daj_dam.user.dto.UserDto;

public interface UserService {
    UserDto getById(Long id);

    UserDto editById(Long id, UserDto userDto);

    UserDto create(UserDto userDto);

    void deleteById(Long id);
}
