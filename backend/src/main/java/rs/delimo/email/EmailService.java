package rs.delimo.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendVerificationEmail(@Email String email, String token) {
        String subject = "Email Verification";
        String verificationUrl = "http://localhost:5173/login/verify?token=" + token;

        Context context = new Context();
        context.setVariable("verificationUrl", verificationUrl);

        String content = templateEngine.process("verification-email", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true); // Enable HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void sendResetPasswordEmail(String email, String resetToken) {
        String subject = "Password Reset Request";

        String resetUrl = "http://localhost:5173/reset-password?token=" + resetToken;

        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);
        String content = templateEngine.process("reset-password-email", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset password email", e);
        }
    }
}

