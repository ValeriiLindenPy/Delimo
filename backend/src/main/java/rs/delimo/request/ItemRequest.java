package rs.delimo.request;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import rs.delimo.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer pricePerDay;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @PrePersist
    private void setCreated() {
        this.created = LocalDateTime.now();
    }
}
