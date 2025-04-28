package rs.delimo.request.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.MyItemRequestsApi;
import rs.delimo.api.dto.*;
import rs.delimo.request.application.RequestService;
import rs.delimo.user.domain.User;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MyRequestController implements MyItemRequestsApi {
    private final RequestService service;

    @Override
    public ResponseEntity<RequestOutputDto> createMyRequest(RequestInputDto requestInputDto) {
        return ResponseEntity.ok(service.create(requestInputDto, getCurrentUser()));
    }

    @Override
    public ResponseEntity<Void> deleteMyRequest(UUID id) {
        service.delete(id, getCurrentUser());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RequestOutputDto> getMyRequest(UUID id) {
        return ResponseEntity.ok(service.getByUserAndId(id, getCurrentUser()));
    }

    @Override
    public ResponseEntity<RequestPageResponse> listMyRequests(Integer page, Integer pageSize) {
        return ResponseEntity.ok(service.getAllByOwner(page, pageSize, getCurrentUser()));
    }

    @Override
    public ResponseEntity<RequestOutputDto> updateMyRequest(UUID id, RequestInputDto requestInputDto) {
        return ResponseEntity.ok(service.edit(id, requestInputDto, getCurrentUser()));
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
