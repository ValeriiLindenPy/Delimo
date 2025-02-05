package rs.delimo.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.delimo.error.exception.DublicatingEmailException;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.item.ItemRepository;
import rs.delimo.request.RequestRepository;
import rs.delimo.user.User;
import rs.delimo.user.UserMapper;
import rs.delimo.user.UserRepository;
import rs.delimo.user.UserService;
import rs.delimo.user.dto.UserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    @Override
    public UserDto getById(Long id) {
        return userRepository.findById(id).map(UserMapper::toUserDto)
                .orElseThrow(
                        () -> new NotFoundException("User with id - %d not found".formatted(id))
                );
    }

    @Override
    @Transactional
    public UserDto editById(Long id, UserDto userDto) {
        log.trace("start edit");

        User oldUser = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id - %d not found".formatted(id))
        );

        log.trace("userid - {}", oldUser.getId());

        if (userDto.getEmail() != null && !oldUser.getEmail().equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                log.info("error email");
                throw new DublicatingEmailException("User with email '%s' already exists"
                        .formatted(userDto.getEmail()));
            }
            oldUser.setEmail(userDto.getEmail());
        }

        log.trace("check other data");

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }


        if (userDto.getStreet() != null) {
            oldUser.setStreet(userDto.getStreet());
        }

        if (userDto.getCity() != null) {
            oldUser.setCity(userDto.getCity());
        }

        if (userDto.getPhone() != null) {
            oldUser.setPhone(userDto.getPhone());
        }

        if (userDto.getViber() != null) {
            oldUser.setViber(userDto.getViber());
        }

        log.trace("check other data completed");

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
    @Transactional
    public void deleteById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        itemRepository.deleteByOwner(user);
        requestRepository.deleteByRequester(user);
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getByUserAuth(User user) {

        User userDb = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        return UserMapper.toUserDto(userDb);
    }


    private boolean isDistinctEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
