package rs.delimo.request.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import rs.delimo.request.ItemRequest;
import rs.delimo.request.RequestMapper;
import rs.delimo.request.RequestRepository;
import rs.delimo.request.RequestService;
import rs.delimo.request.dto.RequestDto;
import rs.delimo.request.dto.RequestForResponseDto;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.util.Objects;


@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RequestForResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemRequest> requests = requestRepository.findAll(pageable);
        return requests.map(RequestMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RequestForResponseDto create(RequestDto request, OidcUser user) {
        User requester = userRepository.findByEmail(user.getAttribute("email"))
                .orElseThrow(() -> new NotFoundException("User not found"));

        updateUserContactInfo(request, requester);

        ItemRequest itemRequest = ItemRequest.builder()
                .description(request.getDescription())
                .title(request.getTitle())
                .pricePerDay(request.getPricePerDay())
                .requester(requester)
                .build();

        return RequestMapper.toResponseDto(requestRepository.save(itemRequest));
    }

    @Override
    @Transactional
    public RequestForResponseDto edit(Long requestID, RequestDto request, OidcUser user) {
        User requester = userRepository.findByEmail(user.getAttribute("email"))
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest OldItemRequest = requestRepository.findById(requestID)
                .orElseThrow(() -> new NotFoundException("Request with id - %d not found".formatted(requestID)));

        updateUserContactInfo(request, requester);

        if (!Objects.equals(OldItemRequest.getRequester().getId(), requester.getId())) {
            throw new OwnerException("Request with id %d does not belong to user with id %d"
                    .formatted(requestID, requester.getId()));
        }

        if (request.getTitle() != null && !request.getTitle().equals(OldItemRequest.getTitle())) {
            OldItemRequest.setTitle(request.getTitle());
        }
        if (request.getDescription() != null && !request.getDescription().equals(OldItemRequest.getDescription())) {
            OldItemRequest.setDescription(request.getDescription());
        }
        if (request.getPricePerDay() != null && !request.getPricePerDay().equals(OldItemRequest.getPricePerDay())) {
            OldItemRequest.setPricePerDay(request.getPricePerDay());
        }

        return RequestMapper.toResponseDto(requestRepository.save(OldItemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public RequestForResponseDto getById(Long requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id - %d not found".formatted(requestId)));
        return RequestMapper.toResponseDto(request);
    }

    @Override
    public void delete(Long requestID, OidcUser user) {
        User requester = userRepository.findByEmail(user.getAttribute("email"))
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest request = requestRepository.findById(requestID)
                .orElseThrow(() -> new NotFoundException("Request with id - %d not found".formatted(requestID)));

        if (!Objects.equals(request.getRequester().getId(), requester.getId())) {
            throw new OwnerException("Request with id %d does not belong to user with id %d"
                    .formatted(requestID, requester.getId()));
        }
        requestRepository.deleteById(requestID);
    }

    private void updateUserContactInfo(RequestDto request, User requester) {
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
            userRepository.save(requester);
        }
    }
}
