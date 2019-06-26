package net.edt.web.exception;

public class InvalidUUIDException extends RuntimeException {

    public InvalidUUIDException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
