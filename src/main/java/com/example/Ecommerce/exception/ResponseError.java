package com.example.Ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseError {

  private String field;
  private String message;

  // error 를 ResponseError 로 변경하는 method
  public static ResponseError of(FieldError e) {
    return ResponseError.builder()
        .message(e.getDefaultMessage())
        .field(((FieldError) e).getField())
        .build();
  }
}