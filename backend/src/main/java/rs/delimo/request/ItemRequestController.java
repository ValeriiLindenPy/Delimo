package rs.delimo.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import rs.delimo.request.dto.RequestOutputDto;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;

    @GetMapping
    public Page<RequestOutputDto> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "6") int pageSize,
                                         @RequestParam(required = false) String city) {
        return requestService.getAll(city, page, pageSize);
    }

    @GetMapping("/{requestId}")
    public RequestOutputDto getOne(@PathVariable Long requestId) {
        return requestService.getById(requestId);
    }

    @GetMapping("/search")
    public Page<RequestOutputDto> search(@RequestParam("text") String text,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "6") int pageSize,
                                         @RequestParam(required = false) String city) {
        return requestService.search(city, text, page, pageSize);
    }
}
