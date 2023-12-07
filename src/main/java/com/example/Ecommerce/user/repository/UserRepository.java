package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUserId(String userId);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);
}
