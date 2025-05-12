package rs.delimo.email.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rs.delimo.api.controller.FeedbackApi;
import rs.delimo.api.dto.FeedbackDto;
import rs.delimo.email.application.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController implements FeedbackApi {
    private final EmailService service;

    @Override
    public ResponseEntity<Void> sendFeedback(FeedbackDto feedbackDto) {
        log.debug("Received feedback: {}", feedbackDto);
        service.sendFeedback(feedbackDto);
        log.debug("Feedback sent successfully");
        return ResponseEntity.noContent().build();
    }
}
