package com.example.Ecommerce.order.repository;

import com.example.Ecommerce.order.domain.OrderProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductOptionRepository extends JpaRepository<OrderProductOption, Long> {

}
