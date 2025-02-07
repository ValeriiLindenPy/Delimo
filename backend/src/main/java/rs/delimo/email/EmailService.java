package rs.delimo.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import rs.delimo.email.dto.FeedbackDto;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendFeedback(FeedbackDto feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("info@delimo.rs");
        message.setSubject(feedback.getSubject());
        message.setText("Email: " + feedback.getEmail() + "\n\n" + "Name: " + feedback.getName() + "\n\n" + feedback.getDescription());
        message.setFrom("customer@delimo.rs");

        mailSender.send(message);
    }

    public void sendVerificationEmail(@Email String email, String token) {
        String subject = "Email Verification";
        String verificationUrl = frontendUrl + "/login?verificationToken=" + token;

        Context context = new Context();
        context.setVariable("verificationUrl", verificationUrl);

        String content = templateEngine.process("verification-email", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("info@delimo.rs");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void sendResetPasswordEmail(String email, String resetToken) {
        String subject = "Password Reset Request";

        String resetUrl = frontendUrl + "/reset-password?resetToken=" + resetToken;

        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);
        String content = templateEngine.process("reset-password-email", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("info@delimo.rs");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset password email", e);
        }
    }
}

