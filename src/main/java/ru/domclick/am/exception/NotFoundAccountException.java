package ru.domclick.am.exception;

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException(String error) {
        super(error);
    }
}
