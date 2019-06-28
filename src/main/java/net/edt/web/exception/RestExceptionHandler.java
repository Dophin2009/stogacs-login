package net.edt.web.exception;

import net.edt.web.transfer.ServiceError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ServiceError errorResponse = new ServiceError(HttpStatus.BAD_REQUEST, "Invalid request body", ex);
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ServiceError errorResponse = new ServiceError(HttpStatus.NOT_FOUND, "Requested entity not found", ex);
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex) {
        ServiceError errorResponse = new ServiceError(HttpStatus.BAD_REQUEST, "Invalid format in request", ex);
        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ServiceError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }

}
