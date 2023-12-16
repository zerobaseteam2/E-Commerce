package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

  boolean existsByUser(User user);
}
