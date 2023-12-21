package com.example.Ecommerce.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDate;

// DB에 저장하는 것으로 변경
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {
  
  @Id
  private String id;
  
  private String refreshToken;
  
  @CreatedDate
  private LocalDate createdDate;
  
  public static RefreshToken createRefreshToken(String username, String refreshToken) {
    return RefreshToken.builder()
            .id(username)
            .refreshToken(refreshToken)
            .build();
  }
}
