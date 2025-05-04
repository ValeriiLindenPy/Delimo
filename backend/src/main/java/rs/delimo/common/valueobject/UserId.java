package rs.delimo.common.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.UUID;

@Validated
@Embeddable
public record UserId(@NotNull UUID value) implements Serializable {
    @JsonCreator
    public static UserId fromString(String id) {
        return new UserId(UUID.fromString(id));
    }
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}
