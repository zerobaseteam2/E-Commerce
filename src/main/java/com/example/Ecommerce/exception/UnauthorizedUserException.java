package com.example.Ecommerce.exception;

public class UnauthorizedUserException extends RuntimeException {

  public UnauthorizedUserException(String s) {
    super(s);
  }
}
