package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
  Optional<RefreshToken> findById(String username);
}
