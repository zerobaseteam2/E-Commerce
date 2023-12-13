package com.example.Ecommerce.user.dto;

import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

public class UserRegisterDto {
  
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;
    
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,12}$")
    private String password;
    
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
    
    @NotBlank
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    
    @Size(max = 11)
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phone;
    
    @Past(message = "생일은 현재보다 이전이어야 합니다.")
    private Date birth;
    
    //    @NotBlank(message = "권한은 필수 입력값입니다.")
    private UserRole role;
    
    public User toEntity(String encryptedPassword) {
      return User.builder()
              .userId(userId)
              .password(encryptedPassword)
              .name(name)
              .email(email)
              .phone(phone)
              .birth(birth)
              .role(role)
              .emailVerify(false)
              .createdAt(new Date())
              .build();
    }
  }
  
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    
    private String userId;
  }
}
