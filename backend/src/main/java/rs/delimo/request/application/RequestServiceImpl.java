package rs.delimo.request.application;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.delimo.api.dto.*;
import rs.delimo.common.client.UserClient;
import rs.delimo.common.exception.NotFoundException;
import rs.delimo.common.exception.OwnerException;
import rs.delimo.common.valueobject.RequestId;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.request.domain.ItemRequest;
import rs.delimo.request.infrastructure.mapper.RequestMapper;
import rs.delimo.request.infrastructure.repository.RequestRepository;
import rs.delimo.user.domain.User;
import rs.delimo.user.infrastructure.repository.UserRepository;
import static rs.delimo.request.domain.specification.RequestSpecification.from;
import java.util.Map;
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
    private final RequestMapper mapper;
    private final UserClient userClient;

    /**
     * Searches for item requests based on a text query and an optional city filter.
     * The results are paginated and sorted in descending order by creation time.
     * @return a page of {@link RequestPageResponse} representing the search results
     */
    @Override
    @Transactional(readOnly = true)
    public RequestPageResponse search(Integer page, Integer size, RequestFilterDto filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<ItemRequest> requests = requestRepository.findAll(from(filter), pageable);

        PageResponse pageResponse = new PageResponse()
                .pageNumber(requests.getNumber())
                .pageSize(requests.getSize())
                .totalPages(requests.getTotalPages())
                .totalElements(requests.getTotalElements())
                .hasNext(requests.hasNext());

        Map<UserId, UserDto> users = userClient.findByIds(
                requests.getContent().stream()
                        .map(ItemRequest::getRequester)
                        .toList()
        );

        return new RequestPageResponse(
                pageResponse,
                requests.getContent()
                        .stream()
                        .map(r -> {
                            RequestOutputDto dto = mapper.toOutputDto(r);
                            dto.setRequester(users.get(r.getRequester()));
                            return dto;
                        })
                        .toList()
        );
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
    public RequestPageResponse getAllByOwner(int page, int pageSize, User user) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created").descending());

        UserDto requester = userClient.findByEmail(user.getEmail());

        Page<ItemRequest> requests = requestRepository.findAllByRequester(new UserId(requester.getId()), pageable);

        PageResponse pageResponse = new PageResponse()
                .pageNumber(requests.getNumber())
                .pageSize(requests.getSize())
                .totalPages(requests.getTotalPages())
                .totalElements(requests.getTotalElements())
                .hasNext(requests.hasNext());

        return new RequestPageResponse(
                pageResponse,
                requests.getContent()
                        .stream()
                        .map(r -> {
                            RequestOutputDto dto = mapper.toOutputDto(r);
                            dto.setRequester(requester);
                            return dto;
                        })
                        .toList()
        );
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
        UserDto requester = userClient.findByEmail(user.getEmail());
        RequestId requestId = new RequestId(id);
        ItemRequest request = requestRepository.findByIdAndRequester(requestId, new UserId(requester.getId())).orElseThrow(
                () -> new NotFoundException("Request with id - %s not found".formatted(id))
        );

        RequestOutputDto dto = mapper.toOutputDto(request);
        dto.setRequester(requester);

        return dto;
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
        UserDto requester = userClient.findByEmail(user.getEmail());

        requester = userClient.updateUserContactInfoByRequestCreate(request, requester);

        ItemRequest itemRequest = ItemRequest.builder()
                .description(request.getDescription())
                .title(request.getTitle())
                .maxPeriodDays(request.getMaxPeriodDays())
                .pricePerDay(request.getPricePerDay())
                .requester(new UserId(requester.getId()))
                .build();

        RequestOutputDto dto = mapper.toOutputDto(requestRepository.save(itemRequest));

        dto.setRequester(requester);

        return dto;
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
    public RequestOutputDto edit(UUID requestID, RequestUpdateDto request, User user) {
        RequestId id = new RequestId(requestID);
        UserDto requester = userClient.findByEmail(user.getEmail());

        ItemRequest oldItemRequest = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request with id - %s not found".formatted(requestID)));

        requester = userClient.updateUserContactInfoByRequestUpdate(request, requester);

        if (!Objects.equals(oldItemRequest.getRequester().value(), requester.getId())) {
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

        RequestOutputDto dto = mapper.toOutputDto(requestRepository.save(oldItemRequest));

        dto.setRequester(requester);

        return dto;
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
        ItemRequest request = requestRepository.findById(new RequestId(requestId))
                .orElseThrow(() -> new NotFoundException("Request with id - %s not found".formatted(requestId)));
        RequestOutputDto dto = mapper.toOutputDto(request);
        dto.setRequester(userClient.findById(request.getRequester()));
        return dto;
    }

    /**
     * Deletes an item request by its ID.
     * Only the owner of the request is allowed to delete it.
     *
     * @param requestId the ID of the request to delete
     * @param user      the user attempting to delete the request
     * @throws NotFoundException if the user or the request is not found
     * @throws OwnerException    if the request does not belong to the user
     */
    @Override
    public void delete(UUID requestId, User user) {
        UserDto requester = userClient.findByEmail(user.getEmail());

        ItemRequest request = requestRepository.findById(new RequestId(requestId))
                .orElseThrow(() -> new NotFoundException("Request with id - %s not found".formatted(requestId)));

        if (!Objects.equals(request.getRequester().value(), requester.getId())) {
            throw new OwnerException("Request with id %s does not belong to user with id %s"
                    .formatted(requestId, requester.getId()));
        }
        requestRepository.deleteById(new RequestId(requestId));
    }
}
