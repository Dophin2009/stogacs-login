package net.edt.web.exception;

public class QRGenerationFailedException extends RuntimeException {

    public QRGenerationFailedException() {
    }

    public QRGenerationFailedException(String message) {
        super(message);
    }

    public QRGenerationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRGenerationFailedException(Throwable cause) {
        super(cause);
    }

    public QRGenerationFailedException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
