package rs.delimo.email;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.delimo.email.dto.FeedbackDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/feedback")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid FeedbackDto feedback) {
        log.warn("Feedback: {}", feedback.toString());
        try {
            emailService.sendFeedback(feedback);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }
}
