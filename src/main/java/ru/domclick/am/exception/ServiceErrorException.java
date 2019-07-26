package ru.domclick.am.exception;

public class ServiceErrorException extends RuntimeException {
    public ServiceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceErrorException(String message) {
        super(message);
    }
}
