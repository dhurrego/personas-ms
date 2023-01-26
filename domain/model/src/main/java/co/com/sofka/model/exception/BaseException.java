package co.com.sofka.model.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final int httpStatusCode;

    public BaseException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
