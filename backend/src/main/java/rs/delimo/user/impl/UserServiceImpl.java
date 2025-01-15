package rs.delimo.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.delimo.error.exception.DublicatingEmailException;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.user.User;
import rs.delimo.user.UserMapper;
import rs.delimo.user.UserRepository;
import rs.delimo.user.UserService;
import rs.delimo.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getById(Long id) {
        return userRepository.findById(id).map(UserMapper::toUserDto)
                .orElseThrow(
                        () -> new NotFoundException("User with id - %d not found".formatted(id))
                );
    }

    @Override
    public UserDto editById(Long id, UserDto userDto) {

        User oldUser = userRepository.findById(id).orElseThrow(
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

        return UserMapper.toUserDto(userRepository.save(oldUser));
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
        userRepository.deleteById(id);
    }


    private boolean isDistinctEmail(String email) {
        return userRepository.findAll().stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }
}
