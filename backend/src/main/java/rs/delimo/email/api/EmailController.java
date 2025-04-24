package rs.delimo.email.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.FeedbackApi;
import rs.delimo.api.dto.FeedbackDto;
import rs.delimo.email.application.EmailService;

@RestController
@RequiredArgsConstructor
public class EmailController implements FeedbackApi {
    private final EmailService service;

    @Override
    public ResponseEntity<Void> sendFeedback(FeedbackDto feedbackDto) {
        service.sendFeedback(feedbackDto);
        return ResponseEntity.noContent().build();
    }
}
