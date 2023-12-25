package com.example.Ecommerce.exception;

public class InvalidOrderStatusException extends RuntimeException {

  public InvalidOrderStatusException(String s) {
    super(s);
  }
}
