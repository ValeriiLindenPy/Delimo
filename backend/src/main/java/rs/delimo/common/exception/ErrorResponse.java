package rs.delimo.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    String error;
    public ErrorResponse(String error) {
        this.error = error;
    }
}
