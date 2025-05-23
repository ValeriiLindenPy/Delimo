package rs.delimo.request.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.delimo.common.valueobject.RequestId;
import rs.delimo.common.valueobject.UserId;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @EmbeddedId
    @Builder.Default
    private RequestId id = RequestId.generate();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer pricePerDay;

    @Column(nullable = false)
    private Integer maxPeriodDays;

    @Embedded
    @AttributeOverride(name = "value",column = @Column(name = "requester_id"))
    private UserId requester;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = new RequestId(UUID.randomUUID());
        }

        this.created = LocalDateTime.now();
    }
}
