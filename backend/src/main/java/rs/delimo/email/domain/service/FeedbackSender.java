package rs.delimo.email.domain.service;

import rs.delimo.email.domain.FeedBack;

public interface FeedbackSender {
    void send(FeedBack feedBack);
}
