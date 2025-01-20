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
                                         @RequestParam(defaultValue = "6") int pageSize) {
        return requestService.getAll(page, pageSize);
    }

    @GetMapping("/{requestId}")
    public RequestOutputDto getOne(@PathVariable Long requestId) {
        return requestService.getById(requestId);
    }
}
