package rs.delimo.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.delimo.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @PrePersist
    private void setCreated() {
        this.created = LocalDateTime.now();
    }
}
