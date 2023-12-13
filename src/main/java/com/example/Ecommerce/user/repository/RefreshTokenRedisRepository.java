package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
  Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
