package rs.delimo.request.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.MyItemRequestsApi;
import rs.delimo.api.dto.*;
import rs.delimo.common.client.SecurityClient;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.request.application.RequestService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyRequestController implements MyItemRequestsApi {
    private final RequestService service;
    private final SecurityClient securityClient;

    @Override
    public ResponseEntity<RequestOutputDto> createMyRequest(RequestInputDto requestInputDto) {
        UserId requesterId = new UserId(securityClient.getCurrentUserId());
        log.debug("Creating request for user={} with data={}", requesterId, requestInputDto);
        RequestOutputDto created = service.create(requestInputDto, requesterId);
        log.debug("Created request: {}", created);
        return ResponseEntity.ok(created);
    }

    @Override
    public ResponseEntity<Void> deleteMyRequest(UUID id) {
        UserId requesterId = new UserId(securityClient.getCurrentUserId());
        log.debug("Deleting request with id={} for user={}", id, requesterId);
        service.delete(id, requesterId);
        log.debug("Request deleted");
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RequestOutputDto> getMyRequest(UUID id) {
        UserId requesterId = new UserId(securityClient.getCurrentUserId());
        log.debug("Fetching request with id={} for user={}", id, requesterId);
        RequestOutputDto result = service.getByUserAndId(id, requesterId);
        log.debug("Retrieved request: {}", result);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<RequestPageResponse> listMyRequests(Integer page, Integer pageSize) {
        UserId requesterId = new UserId(securityClient.getCurrentUserId());
        log.debug("Listing requests for user={}, page={}, size={}", requesterId, page, pageSize);
        RequestPageResponse response = service.getAllByOwner(page, pageSize, requesterId);
        log.debug("Listed {} requests", response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RequestOutputDto> updateMyRequest(UUID id, RequestUpdateDto requestInputDto) {
        UserId requesterId = new UserId(securityClient.getCurrentUserId());
        log.debug("Updating request with id={} for user={}, data={}", id, requesterId, requestInputDto);
        RequestOutputDto updated = service.edit(id, requestInputDto, requesterId);
        log.debug("Updated request: {}", updated);
        return ResponseEntity.ok(updated);
    }
}
