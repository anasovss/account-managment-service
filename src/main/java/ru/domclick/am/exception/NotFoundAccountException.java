package ru.domclick.am.exception;

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException(String accountNumber) {
        super("Could not find account by number: "+ accountNumber);
    }
}
