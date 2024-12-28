package com.dajdam.daj_dam.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dajdam.daj_dam.error.exception.DublicatingEmailException;
import com.dajdam.daj_dam.error.exception.NotFoundException;
import com.dajdam.daj_dam.user.User;
import com.dajdam.daj_dam.user.UserMapper;
import com.dajdam.daj_dam.user.UserRepository;
import com.dajdam.daj_dam.user.UserService;
import com.dajdam.daj_dam.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getById(Long id) {
        return userRepository.findOne(id).map(UserMapper::toUserDto)
                .orElseThrow(
                        () -> new NotFoundException("User with id - %d not found".formatted(id))
                );
    }

    @Override
    public UserDto editById(Long id, UserDto userDto) {

        User oldUser = userRepository.findOne(id).orElseThrow(
                () -> new NotFoundException("User with id - %d not found".formatted(id))
        );

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (isDistinctEmail(userDto.getEmail())) {
                oldUser.setEmail(userDto.getEmail());
            } else {
                throw new DublicatingEmailException("User with email - %s is already exist"
                        .formatted(userDto.getEmail()));
            }
        }

        return UserMapper.toUserDto(userRepository.update(oldUser));
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (!isDistinctEmail(userDto.getEmail())) {
            throw new DublicatingEmailException("User with email - %s is already exist"
                    .formatted(userDto.getEmail()));
        }
        return UserMapper.toUserDto(userRepository
                .save(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        userRepository.delete(id);
    }

    @Override
    public void clear() {
        userRepository.clear();
    }

    private boolean isDistinctEmail(String email) {
        return userRepository.findAll().stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }
}
