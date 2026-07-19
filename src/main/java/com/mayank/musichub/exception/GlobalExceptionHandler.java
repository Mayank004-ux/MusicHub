package com.mayank.musichub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // This class will handle exceptions  for all controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleException(Exception ex) {

    ErrorResponse error =
            new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ex.getMessage());

    return new ResponseEntity<>(
            error,
            HttpStatus.INTERNAL_SERVER_ERROR
    );
}

}