package net.edt.web.exception;

import net.edt.web.dto.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
        StringBuilder debugMessageBuilder = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            debugMessageBuilder.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            debugMessageBuilder.append(error.getObjectName()).append(": ").append(error.getDefaultMessage()).append("; ");
        }

        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.BAD_REQUEST, "Invalid request body", debugMessageBuilder.toString());
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.NOT_FOUND, "Requested entity not found", ex);
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.NOT_FOUND, "Entity already exists", ex);
        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex) {
        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.BAD_REQUEST, "Invalid format in request", ex);
        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorMessage error) {
        return new ResponseEntity<>(error, error.getStatus());
    }

}
