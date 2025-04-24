package rs.delimo.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.delimo.api.dto.UserDto;
import rs.delimo.common.exception.DuplicatingEmailException;
import rs.delimo.common.exception.NotFoundException;
import rs.delimo.item.infrastructure.repository.ItemRepository;
import rs.delimo.request.infrastructure.repository.RequestRepository;
import rs.delimo.user.domain.User;
import rs.delimo.user.infrastructure.mapper.UserMapper;
import rs.delimo.user.infrastructure.repository.UserRepository;

import java.util.UUID;


/**
 * Implementation of the {@link rs.delimo.user.application.UserService} interface providing user-related operations.
 * <p>
 * This service is responsible for fetching, editing, and deleting users as well as loading users by username.
 * It uses {@link UserRepository} for user persistence, {@link ItemRepository} for managing user-owned items,
 * and {@link RequestRepository} for handling user-related requests.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    /**
     * Retrieves a {@link UserDto} by the given user id.
     *
     * @param id the unique identifier of the user.
     * @return the {@link UserDto} corresponding to the given id.
     * @throws NotFoundException if no user is found with the provided id.
     */
    @Override
    public UserDto getById(UUID id) {
        log.info("Fetching user by id: {}", id);
        UserDto userDto = userRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new NotFoundException("User with id - %d not found".formatted(id));
                });
        log.info("Retrieved user: {}", userDto);
        return userDto;
    }

    /**
     * Edits an existing user's information based on the provided {@link UserDto}.
     *
     * @param id      the unique identifier of the user to edit.
     * @param userDto the data transfer object containing the new user details.
     * @return the updated {@link UserDto} after saving the changes.
     * @throws NotFoundException        if no user is found with the provided id.
     * @throws DuplicatingEmailException if the new email already exists for another user.
     */
    @Override
    @Transactional
    public UserDto editById(UUID id, UserDto userDto) {
        log.trace("Starting edit of user with id: {}", id);

        User oldUser = userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found for editing", id);
            return new NotFoundException("User with id - %d not found".formatted(id));
        });

        log.trace("User found. Current user id: {}", oldUser.getId());

        if (userDto.getEmail() != null && !oldUser.getEmail().equals(userDto.getEmail())) {
            log.debug("Attempting to update email from '{}' to '{}'", oldUser.getEmail(), userDto.getEmail());
            if (userRepository.existsByEmail(userDto.getEmail())) {
                log.info("Email '{}' already exists", userDto.getEmail());
                throw new DuplicatingEmailException("User with email '%s' already exists".formatted(userDto.getEmail()));
            }
            oldUser.setEmail(userDto.getEmail());
        }

        log.trace("Checking other data for update");

        if (userDto.getName() != null) {
            log.debug("Updating name to '{}'", userDto.getName());
            oldUser.setName(userDto.getName());
        }
        if (userDto.getStreet() != null) {
            log.debug("Updating street to '{}'", userDto.getStreet());
            oldUser.setStreet(userDto.getStreet());
        }
        if (userDto.getCity() != null) {
            log.debug("Updating city to '{}'", userDto.getCity());
            oldUser.setCity(userDto.getCity());
        }
        if (userDto.getPhone() != null) {
            log.debug("Updating phone to '{}'", userDto.getPhone());
            oldUser.setPhone(userDto.getPhone());
        }
        if (userDto.getViber() != null) {
            log.debug("Updating Viber to '{}'", userDto.getViber());
            oldUser.setViber(userDto.getViber());
        }

        log.trace("Completed checking data for update");

        UserDto updatedUserDto = mapper.toDto(userRepository.save(oldUser));
        log.info("User with id {} updated successfully", id);
        return updatedUserDto;
    }

    /**
     * Deletes a user by the provided user id. This operation also removes associated items and requests.
     *
     * @param userId the unique identifier of the user to be deleted.
     * @throws NotFoundException if no user is found with the provided id.
     */
    @Override
    @Transactional
    public void deleteById(UUID userId) {
        log.info("Deleting user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found for deletion", userId);
                    return new NotFoundException("User not found");
                });
        log.debug("Deleting items for user id: {}", userId);
        itemRepository.deleteByOwner(user);
        log.debug("Deleting requests for user id: {}", userId);
        requestRepository.deleteByRequester(user);
        userRepository.deleteById(userId);
        log.info("User with id {} deleted successfully", userId);
    }

    /**
     * Retrieves a {@link UserDto} for an authenticated user.
     *
     * @param user the authenticated {@link User} object.
     * @return the {@link UserDto} corresponding to the authenticated user.
     * @throws NotFoundException if the authenticated user cannot be found in the repository.
     */
    @Override
    public UserDto getByUserAuth(User user) {
        log.info("Fetching authenticated user by email: {}", user.getEmail());
        User userDb = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> {
            log.error("Authenticated user with email {} not found", user.getEmail());
            return new NotFoundException("User not found");
        });
        UserDto userDto = mapper.toDto(userDb);
        log.info("Retrieved authenticated user: {}", userDto);
        return userDto;
    }

    /**
     * Loads a {@link User} based on the provided username (email).
     *
     * @param username the email of the user to be loaded.
     * @return the {@link User} associated with the given username.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username (email): {}", username);
        User user = userRepository.findByEmail(username).orElseThrow(() -> {
            log.error("User not found with email: {}", username);
            return new UsernameNotFoundException("User not found with email: " + username);
        });
        log.info("Loaded user: {}", user);
        return user;
    }
}
