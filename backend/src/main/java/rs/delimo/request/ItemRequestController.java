package rs.delimo.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import rs.delimo.request.dto.RequestOutputDto;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    private final RequestService requestService;

    @GetMapping
    public Page<RequestOutputDto> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "6") int pageSize,
                                         @RequestParam(required = false) String city) {
        log.info("Received request to get all requests: page={}, pageSize={}, city={}", page, pageSize, city);
        Page<RequestOutputDto> requests = requestService.getAll(city, page, pageSize);
        log.info("Retrieved {} requests", requests.getTotalElements());
        return requests;
    }

    @GetMapping("/{requestId}")
    public RequestOutputDto getOne(@PathVariable Long requestId) {
        log.info("Received request to get request with id: {}", requestId);
        RequestOutputDto requestDto = requestService.getById(requestId);
        log.info("Retrieved request: {}", requestDto);
        return requestDto;
    }

    @GetMapping("/search")
    public Page<RequestOutputDto> search(@RequestParam("text") String text,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "6") int pageSize,
                                         @RequestParam(required = false) String city) {
        log.info("Received search request: text='{}', page={}, pageSize={}, city={}", text, page, pageSize, city);
        Page<RequestOutputDto> requests = requestService.search(city, text, page, pageSize);
        log.info("Found {} requests for search text '{}'", requests.getTotalElements(), text);
        return requests;
    }
}
