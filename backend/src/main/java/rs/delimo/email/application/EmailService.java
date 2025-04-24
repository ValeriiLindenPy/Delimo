package rs.delimo.email.application;

import rs.delimo.api.dto.FeedbackDto;

public interface EmailService {
    void sendFeedback(FeedbackDto feedbackDto);
    void sendVerificationEmail(String email, String token);
    void sendResetPasswordEmail(String email, String resetToken);
}
