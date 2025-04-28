package rs.delimo.request.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.ItemRequestsApi;
import rs.delimo.api.dto.RequestFilterDto;
import rs.delimo.api.dto.RequestOutputDto;
import rs.delimo.api.dto.RequestPageResponse;
import rs.delimo.request.application.RequestService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RequestController implements ItemRequestsApi {
    private final RequestService service;
    @Override
    public ResponseEntity<RequestOutputDto> getRequest(UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<RequestPageResponse> listRequests(Integer page, Integer size, RequestFilterDto requestFilterDto) {
        return ResponseEntity.ok(service.search(page, size, requestFilterDto));
    }
}
