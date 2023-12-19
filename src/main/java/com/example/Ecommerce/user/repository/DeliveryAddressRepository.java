package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

  boolean existsByUser(User user);

  Optional<DeliveryAddress> findByUserAndId(User user, Long deliveryAddressId);

  Page<DeliveryAddress> findAllByUser(User user, Pageable pageable);
}
