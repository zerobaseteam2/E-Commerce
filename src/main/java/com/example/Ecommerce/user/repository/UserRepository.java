package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findById(Long id);
  Optional<User> findByUserId(String username);
  
  @Query("SELECT e FROM User e WHERE MONTH(e.birth) = MONTH(:targetDate) AND DAY(e.birth) = DAY(:targetDate)")
  List<User> findByBirthMonthAndDay(@Param("targetDate") LocalDate targetDate);
  
  boolean existsByUserId(String userId);
  
  boolean existsByEmail(String email);
  
  boolean existsByPhone(String phone);
}
