package rs.delimo.request.application;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.delimo.api.dto.RequestInputDto;
import rs.delimo.api.dto.RequestOutputDto;
import rs.delimo.common.exception.NotFoundException;
import rs.delimo.common.exception.OwnerException;
import rs.delimo.request.domain.ItemRequest;
import rs.delimo.request.infrastructure.mapper.RequestMapper;
import rs.delimo.request.infrastructure.repository.RequestRepository;
import rs.delimo.user.domain.User;
import rs.delimo.user.infrastructure.repository.UserRepository;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of the {@link RequestService} interface.
 * <p>
 * Provides methods to perform CRUD operations and search functionalities for item requests.
 * Interacts with {@link RequestRepository} for request-related operations and {@link UserRepository} for user-related operations.
 * </p>
 */
@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper mapper;

    /**
     * Retrieves all item requests with pagination and sorts them in descending order by creation time.
     *
     * @param page the page number to retrieve
     * @param size the size of the page
     * @return a page of {@link RequestOutputDto} representing the item requests
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RequestOutputDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<ItemRequest> requests = requestRepository.findAll(pageable);
        return requests.map(mapper::toOutputDto);
    }

    /**
     * Retrieves all item requests optionally filtered by city with pagination and sorts them in descending order by creation time.
     *
     * @param city     the city to filter requests; if {@code null} or blank, no city filter is applied
     * @param page     the page number to retrieve
     * @param pageSize the size of the page
     * @return a page of {@link RequestOutputDto} representing the filtered item requests
     */
    @Override
    public Page<RequestOutputDto> getAll(String city, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created").descending());
        Page<ItemRequest> requests;
        if (city != null && !city.isBlank()) {
            requests = requestRepository.findAllByCity(pageable, city);
        } else {
            requests = requestRepository.findAll(pageable);
        }
        return requests.map(mapper::toOutputDto);
    }

    /**
     * Searches for item requests based on a text query and an optional city filter.
     * The results are paginated and sorted in descending order by creation time.
     *
     * @param city     the city to filter requests; if {@code null} or blank, no city filter is applied
     * @param text     the text to search within the requests
     * @param page     the page number to retrieve
     * @param pageSize the size of the page
     * @return a page of {@link RequestOutputDto} representing the search results
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RequestOutputDto> search(String city, String text, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created").descending());

        Page<ItemRequest> requests;

        if (city != null && !city.isBlank()) {
            requests = requestRepository.searchWithCity(pageable, text, city);
        } else {
            requests = requestRepository.search(pageable, text);
        }

        return requests.map(mapper::toOutputDto);
    }

    /**
     * Retrieves all item requests belonging to the specified owner.
     * The results are paginated.
     *
     * @param page     the page number to retrieve
     * @param pageSize the size of the page
     * @param user     the owner of the requests
     * @return a page of {@link RequestOutputDto} representing the owner's item requests
     * @throws NotFoundException if the user is not found
     */
    @Override
    public Page<RequestOutputDto> getAllByOwner(int page, int pageSize, User user) {
        Pageable pageable = PageRequest.of(page, pageSize);
        User requester = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Page<ItemRequest> requests = requestRepository.findAllByRequesterId(requester.getId(), pageable);

        return requests.map(mapper::toOutputDto);
    }

    /**
     * Retrieves an item request by its ID for a specific user.
     *
     * @param id   the ID of the item request
     * @param user the user who owns the request
     * @return the {@link RequestOutputDto} representing the item request
     * @throws NotFoundException if the user or the request is not found
     */
    @Override
    public RequestOutputDto getByUserAndId(UUID id, User user) {
        User requester = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        ItemRequest request = requestRepository.findByIdAndRequesterId(id, requester.getId()).orElseThrow(
                () -> new NotFoundException("Request with id - %s not found".formatted(id))
        );
        return mapper.toOutputDto(request);
    }

    /**
     * Creates a new item request based on the provided input data and associates it with the given user.
     * If the user's contact information is incomplete, it is updated with data from the request.
     *
     * @param request the input data for creating the request
     * @param user    the user creating the request
     * @return the created {@link RequestOutputDto} representing the new item request
     * @throws NotFoundException if the user is not found
     */
    @Override
    @Transactional
    public RequestOutputDto create(RequestInputDto request, User user) {
        User requester = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        updateUserContactInfo(request, requester);

        ItemRequest itemRequest = ItemRequest.builder()
                .description(request.getDescription())
                .title(request.getTitle())
                .maxPeriodDays(request.getMaxPeriodDays())
                .pricePerDay(request.getPricePerDay())
                .requester(requester)
                .build();

        return mapper.toOutputDto(requestRepository.save(itemRequest));
    }

    /**
     * Edits an existing item request identified by {@code requestID} using the provided input data.
     * Only the owner of the request is allowed to perform the edit.
     * Also updates the user's contact information if necessary.
     *
     * @param requestID the ID of the request to edit
     * @param request   the new input data for the request
     * @param user      the user attempting to edit the request
     * @return the updated {@link RequestOutputDto} representing the edited request
     * @throws NotFoundException if the user or the request is not found
     * @throws OwnerException    if the request does not belong to the user
     */
    @Override
    @Transactional
    public RequestOutputDto edit(UUID requestID, RequestInputDto request, User user) {
        User requester = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest oldItemRequest = requestRepository.findById(requestID)
                .orElseThrow(() -> new NotFoundException("Request with id - %s not found".formatted(requestID)));

        updateUserContactInfo(request, requester);

        if (!Objects.equals(oldItemRequest.getRequester().getId(), requester.getId())) {
            throw new OwnerException("Request with id %s does not belong to user with id %s"
                    .formatted(requestID, requester.getId()));
        }

        if (request.getTitle() != null && !request.getTitle().equals(oldItemRequest.getTitle())) {
            oldItemRequest.setTitle(request.getTitle());
        }
        if (request.getDescription() != null && !request.getDescription().equals(oldItemRequest.getDescription())) {
            oldItemRequest.setDescription(request.getDescription());
        }
        if (request.getPricePerDay() != null && !request.getPricePerDay().equals(oldItemRequest.getPricePerDay())) {
            oldItemRequest.setPricePerDay(request.getPricePerDay());
        }
        if (request.getMaxPeriodDays() != null && !request.getMaxPeriodDays().equals(oldItemRequest.getMaxPeriodDays())) {
            oldItemRequest.setMaxPeriodDays(request.getMaxPeriodDays());
        }

        return mapper.toOutputDto(requestRepository.save(oldItemRequest));
    }

    /**
     * Retrieves an item request by its ID.
     *
     * @param requestId the ID of the request to retrieve
     * @return the {@link RequestOutputDto} representing the found item request
     * @throws NotFoundException if the request is not found
     */
    @Override
    @Transactional(readOnly = true)
    public RequestOutputDto getById(UUID requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id - %d not found".formatted(requestId)));
        return mapper.toOutputDto(request);
    }

    /**
     * Deletes an item request by its ID.
     * Only the owner of the request is allowed to delete it.
     *
     * @param requestID the ID of the request to delete
     * @param user      the user attempting to delete the request
     * @throws NotFoundException if the user or the request is not found
     * @throws OwnerException    if the request does not belong to the user
     */
    @Override
    public void delete(UUID requestID, User user) {
        User requester = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest request = requestRepository.findById(requestID)
                .orElseThrow(() -> new NotFoundException("Request with id - %d not found".formatted(requestID)));

        if (!Objects.equals(request.getRequester().getId(), requester.getId())) {
            throw new OwnerException("Request with id %s does not belong to user with id %s"
                    .formatted(requestID, requester.getId()));
        }
        requestRepository.deleteById(requestID);
    }

    /**
     * Updates the contact information (city, Viber, and phone) of the requester if the information is missing.
     * Saves the user record if any updates are made.
     *
     * @param request   the {@link RequestInputDto} containing the potential new contact information
     * @param requester the {@link User} whose contact information may be updated
     */
    private void updateUserContactInfo(RequestInputDto request, User requester) {
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
