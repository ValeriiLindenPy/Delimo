package rs.delimo.item;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import rs.delimo.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Boolean available;
    private Integer maxPeriodDays;
    private Integer pricePerDay;

    @ElementCollection
    private List<String> images;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
