package com.example.Ecommerce.order.repository;

import com.example.Ecommerce.order.domain.OrderStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
  Page<OrderStatusHistory> findAllByOrderProduct_Id(Long orderProductId, Pageable pageable);

}


