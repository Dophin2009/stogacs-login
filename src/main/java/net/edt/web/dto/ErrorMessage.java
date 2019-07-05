package net.edt.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorMessage {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ss")
    private LocalDateTime timestamp;
    private String error;
    private String message;

    private ErrorMessage() {
        timestamp = LocalDateTime.now();
    }

    public ErrorMessage(HttpStatus status) {
        this();
        this.status = status;
    }

    public ErrorMessage(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.error = "Unexpected error";
        this.message = ex.getLocalizedMessage();
    }

    public ErrorMessage(HttpStatus status, String error, Throwable ex) {
        this();
        this.status = status;
        this.error = error;
        this.message = ex.getLocalizedMessage();
    }

    public ErrorMessage(HttpStatus status, String error, String message) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
