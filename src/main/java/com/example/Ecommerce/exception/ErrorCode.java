package com.example.Ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found.");
    
    private HttpStatus status;
    private String message;
}
