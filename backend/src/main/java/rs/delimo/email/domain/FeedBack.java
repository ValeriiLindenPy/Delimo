package rs.delimo.email.domain;

import lombok.Builder;

@Builder
public record FeedBack(
        String name,
        String email,
        String subject,
        String description
) {
}
