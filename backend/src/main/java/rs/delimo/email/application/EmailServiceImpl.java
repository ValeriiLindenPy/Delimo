package rs.delimo.email.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import rs.delimo.api.dto.FeedbackDto;
import rs.delimo.email.domain.service.FeedbackSender;
import rs.delimo.email.infrastructure.mapper.FeedBackMapper;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final FeedbackSender feedbackSender;
    private final FeedBackMapper feedBackMapper;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void sendFeedback(FeedbackDto feedbackDto) {
        feedbackSender.send(feedBackMapper.toFeedBack(feedbackDto));
    }

    @Override
    public void sendVerificationEmail(String email, String token) {
        String subject = "Email Verification";
        String url = frontendUrl + "/login?verificationToken=" + token;
        var context = new Context();
        context.setVariable("verificationUrl", url);
        String content = templateEngine.process("verification-email", context);

        sendHtml(email, subject, content);
    }

    @Override
    public void sendResetPasswordEmail(String email, String resetToken) {
        String subject = "Password Reset Request";
        String url = frontendUrl + "/reset-password?resetToken=" + resetToken;
        var context = new Context();
        context.setVariable("resetUrl", url);
        String content = templateEngine.process("reset-password-email", context);

        sendHtml(email, subject, content);
    }

    private void sendHtml(String to, String subject, String html) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
