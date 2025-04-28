package rs.delimo.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.delimo.common.valueobject.ItemId;
import rs.delimo.common.valueobject.UserId;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @EmbeddedId
    private ItemId id;
    private String title;
    private String description;
    private Boolean available;
    private Integer maxPeriodDays;
    private Integer pricePerDay;

    @ElementCollection
    @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "image_url")
    private List<String> images;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "owner_id"))
    private UserId owner;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @PrePersist
    private void onCreate() {
        this.created = LocalDateTime.now();
    }
}
