package com.example.Ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> customExceptionHandler(CustomException e) {
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.toString());
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<String> orderNotFoundExceptionHandler(OrderNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(InvalidOrderStatusException.class)
  public ResponseEntity<String> invalidOrderStatusExceptionHandler(InvalidOrderStatusException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(UnauthorizedUserException.class)
  public ResponseEntity<String> unAuthorizedUserExceptionHandler(UnauthorizedUserException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  @ExceptionHandler(InvalidQuantityException.class)
  public ResponseEntity<String> invalidQuantityExceptionHandler(InvalidQuantityException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }
}
