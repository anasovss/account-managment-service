package ru.domclick.am.exception;

public class BadRequestAccountException extends RuntimeException {
    public BadRequestAccountException(String error) {
        super(error);
    }
}
