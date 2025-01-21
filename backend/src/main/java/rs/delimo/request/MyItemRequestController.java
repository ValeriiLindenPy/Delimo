package rs.delimo.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.error.ValidationMarker;
import rs.delimo.request.dto.RequestInputDto;
import rs.delimo.request.dto.RequestOutputDto;
import rs.delimo.user.User;


@RestController
@RequestMapping(path = "/my-requests")
@RequiredArgsConstructor
public class MyItemRequestController {
    private final RequestService requestService;

    @GetMapping
    public Page<RequestOutputDto> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "6") int pageSize,
                                         @AuthenticationPrincipal User user) {
        return requestService.getAllByOwner(page, pageSize, user);
    }

    @GetMapping("/{id}")
    public RequestOutputDto getOne(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return requestService.getByUserAndId(id, user);
    }

    @PostMapping
    public RequestOutputDto create(@Validated(ValidationMarker.OnCreate.class) @Valid @RequestBody RequestInputDto request,
                                   @AuthenticationPrincipal User user) {
        return requestService.create(request, user);
    }

    @PatchMapping("/{id}")
    public RequestOutputDto edit(@RequestBody RequestInputDto request,
                                 @PathVariable Long id,
                                 @AuthenticationPrincipal User user) {
        return requestService.edit(id, request, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        requestService.delete(id, user);
    }
}
