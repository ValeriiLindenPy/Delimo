package rs.delimo.email.infrastructure.mail;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import rs.delimo.email.domain.FeedBack;
import rs.delimo.email.domain.service.FeedbackSender;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class SpringMailFeedbackSender implements FeedbackSender {
    private final JavaMailSender mailSender;

    @Value("${app.email.to:info@delimo.rs}")
    private String toAddress;

    @Value("${app.email.from:customer@delimo.rs}")
    private String fromAddress;

    @Override
    public void send(FeedBack feedback) {
        var msg = new SimpleMailMessage();
        msg.setTo(toAddress);
        msg.setFrom(fromAddress);
        msg.setSubject(feedback.subject());
        msg.setText(
                "Email: " + feedback.email() + "\n\n" +
                        "Name: " + feedback.name() + "\n\n" +
                        feedback.description()
        );
        mailSender.send(msg);
    }
}
