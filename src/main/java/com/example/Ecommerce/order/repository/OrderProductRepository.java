package com.example.Ecommerce.order.repository;

import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

  Page<OrderProduct> findAllByOrderIdIn(List<Long> orderIds, Pageable pageable);
  Page<OrderProduct> findAllByProductSellerIdAndStatus(Long sellerId, OrderStatus status, Pageable pageable);
  Page<OrderProduct> findAllByProductSellerId(Long sellerId, Pageable pageable);

}
