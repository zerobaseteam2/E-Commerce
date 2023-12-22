package com.example.Ecommerce.order.repository;

import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findAllByUser(Optional<User> user);
  Optional<Order> findOrderByProductId(Long orderProductId);
}
