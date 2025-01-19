package rs.delimo.request;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.delimo.request.dto.RequestDto;
import rs.delimo.request.dto.RequestForResponseDto;


@RestController
@RequestMapping(path = "/my-requests")
@RequiredArgsConstructor
public class MyItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestForResponseDto create(@RequestBody @Validated RequestDto request,
                                        @AuthenticationPrincipal OidcUser user) {
        return requestService.create(request, user);
    }

    @PatchMapping("/{requestID}")
    public RequestForResponseDto edit(@RequestBody @Validated RequestDto request,
                                      @PathVariable Long requestID,
                                      @AuthenticationPrincipal OidcUser user) {
        return requestService.edit(requestID, request, user);
    }

    @DeleteMapping("/{requestID}")
    public void delete(@PathVariable Long requestID, @AuthenticationPrincipal OidcUser user) {
        requestService.delete(requestID, user);
    }
}
