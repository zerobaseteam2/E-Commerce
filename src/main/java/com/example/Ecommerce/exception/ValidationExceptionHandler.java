package com.example.Ecommerce.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ResponseError>> handleValidationException(
      MethodArgumentNotValidException ex) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    List<ResponseError> errors = fieldErrors.stream()
        .map(e -> ResponseError.of(e))
        .collect(Collectors.toList());
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
