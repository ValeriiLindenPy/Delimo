package rs.delimo.user.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rs.delimo.api.dto.*;
import rs.delimo.common.client.UserClient;
import rs.delimo.common.exception.NotFoundException;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.user.domain.User;
import rs.delimo.user.infrastructure.mapper.UserMapper;
import rs.delimo.user.infrastructure.repository.UserRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {
    private final UserRepository repository;
    private final UserMapper mapper;


    @Override
    public Map<UserId, UserDto> findByIds(Collection<UserId> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<User> users = repository.findAllById(userIds);
        Map<UserId, UserDto> map = new HashMap<>();
        users.forEach(user -> map.put(user.getId(), mapper.toDto(user)));
        return map;
    }

    @Override
    public UserDto findById(UserId id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found: " + id.value()));
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return mapper.toDto(user);
    }

    @Override
    public Set<UserId> findIdsByCity(String city) {
        return Set.of();
    }

    @Override
    public UserDto updateUserContactInfoByRequestUpdate(RequestUpdateDto request, UserDto dto) {
        User requester = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean updated = false;

        if (request.getCity() != null && (requester.getCity() == null || requester.getCity().isBlank())) {
            requester.setCity(request.getCity());
            updated = true;
        }

        if (request.getViber() != null && (requester.getViber() == null || requester.getViber().isBlank())) {
            requester.setViber(request.getViber());
            updated = true;
        }
        if (request.getPhone() != null && (requester.getPhone() == null || requester.getPhone().isBlank())) {
            requester.setPhone(request.getPhone());
            updated = true;
        }
        if (updated) {
            repository.save(requester);
        }
        return mapper.toDto(requester);
    }

    @Override
    public UserDto updateUserContactInfoByRequestCreate(RequestInputDto request, UserDto dto) {
        User requester = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean updated = false;
        if (request.getCity() != null && (requester.getCity() == null || requester.getCity().isBlank())) {
            requester.setCity(request.getCity());
            updated = true;
        }

        if (request.getViber() != null && (requester.getViber() == null || requester.getViber().isBlank())) {
            requester.setViber(request.getViber());
            updated = true;
        }
        if (request.getPhone() != null && (requester.getPhone() == null || requester.getPhone().isBlank())) {
            requester.setPhone(request.getPhone());
            updated = true;
        }
        if (updated) {
            repository.save(requester);
        }
        return mapper.toDto(requester);
    }

    @Override
    public UserDto updateUserContactInfoByItemUpdate(ItemUpdateDto item, UserDto dto) {
        User owner = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean updated = false;

        if (item.getCity() != null && (owner.getCity() == null || owner.getCity().isBlank())) {
            owner.setCity(item.getCity());
            updated = true;
        }
        if (item.getStreet() != null && (owner.getStreet() == null || owner.getStreet().isBlank())) {
            owner.setStreet(item.getStreet());
            updated = true;
        }
        if (item.getViber() != null && (owner.getViber() == null || owner.getViber().isBlank())) {
            owner.setViber(item.getViber());
            updated = true;
        }
        if (item.getPhone() != null && (owner.getPhone() == null || owner.getPhone().isBlank())) {
            owner.setPhone(item.getPhone());
            updated = true;
        }
        if (updated) {
            repository.save(owner);
        }

        return mapper.toDto(owner);
    }

    @Override
    public UserDto updateUserContactInfoByItemCreate(ItemRequestDto item, UserDto dto) {
        User owner = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean updated = false;

        if (item.getCity() != null && (owner.getCity() == null || owner.getCity().isBlank())) {
            owner.setCity(item.getCity());
            updated = true;
        }
        if (item.getStreet() != null && (owner.getStreet() == null || owner.getStreet().isBlank())) {
            owner.setStreet(item.getStreet());
            updated = true;
        }
        if (item.getViber() != null && (owner.getViber() == null || owner.getViber().isBlank())) {
            owner.setViber(item.getViber());
            updated = true;
        }
        if (item.getPhone() != null && (owner.getPhone() == null || owner.getPhone().isBlank())) {
            owner.setPhone(item.getPhone());
            updated = true;
        }
        if (updated) {
            repository.save(owner);
        }

        return mapper.toDto(owner);
    }
}
