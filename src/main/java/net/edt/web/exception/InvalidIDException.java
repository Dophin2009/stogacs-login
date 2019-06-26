package net.edt.web.exception;

public class InvalidIDException extends RuntimeException {

    public InvalidIDException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidIDException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
