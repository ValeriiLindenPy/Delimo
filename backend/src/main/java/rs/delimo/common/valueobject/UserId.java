package rs.delimo.common.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.UUID;

@Validated
@Embeddable
public record UserId(@NotNull UUID value) implements Serializable {
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}
