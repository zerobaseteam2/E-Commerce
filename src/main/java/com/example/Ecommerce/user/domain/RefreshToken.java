package com.example.Ecommerce.user.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

// DB에 저장하는 것으로 변경
@Getter
@RedisHash("refreshToken")
@AllArgsConstructor
@Builder
public class RefreshToken {
  
  @Id
  private String id;
  
  private String refreshToken;
  
  @TimeToLive
  private Long expiration; // 설정한 시간만큼 데이터 저장
  
  public static RefreshToken createRefreshToken(String username, String refreshToken, Long remainingMilliSeconds) {
    return RefreshToken.builder()
            .id(username)
            .refreshToken(refreshToken)
            .expiration(remainingMilliSeconds / 1000)
            .build();
  }
}
