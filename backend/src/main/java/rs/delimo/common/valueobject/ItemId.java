package rs.delimo.common.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.UUID;

@Validated
@Embeddable
public record ItemId(@NotNull UUID value) implements Serializable {

    public static ItemId generate() {
        return new ItemId(UUID.randomUUID());
    }
}
