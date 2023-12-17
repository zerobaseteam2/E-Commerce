package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String username);
  
  List<User> findAllByBirth_MonthAndBirth_Day(LocalDate birth);
  
  boolean existsByUserId(String userId);
  
  boolean existsByEmail(String email);
  
  boolean existsByPhone(String phone);
}
