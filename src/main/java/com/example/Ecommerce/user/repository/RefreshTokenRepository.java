package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  Optional<RefreshToken> findById(String username);

  Optional<RefreshToken> findByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);

  boolean existsByRefreshToken(String refreshToken);
}
