package rs.delimo.item.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemTitle {
    private Long id;
    private String title;
}
