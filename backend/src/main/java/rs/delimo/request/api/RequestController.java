package rs.delimo.request.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RequestController implements ItemRequestsApi {
    private final RequestService service;

    @Override
    public ResponseEntity<RequestOutputDto> getRequest(UUID id) {
        log.debug("Fetching request by id={}", id);
        RequestOutputDto request = service.getById(id);
        log.debug("Retrieved request: {}", request);
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity<RequestPageResponse> listRequests(Integer page, Integer size) {
        log.debug("Listing all requests, page={}, size={}", page, size);
        RequestPageResponse response = service.listRequests(page, size);
        log.debug("Listed {} requests", response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RequestPageResponse> searchRequests(Integer page, Integer size, RequestFilterDto requestFilterDto) {
        log.debug("Searching requests, page={}, size={}, filters={}", page, size, requestFilterDto);
        RequestPageResponse result = service.search(page, size, requestFilterDto);
        log.debug("Found {} matching requests", result.getContent().size());
        return ResponseEntity.ok(result);
    }
}
