package rs.delimo.common.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Validated
public record RequestId(@NotNull UUID value) implements Serializable {
}
