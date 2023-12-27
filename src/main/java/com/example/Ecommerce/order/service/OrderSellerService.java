package com.example.Ecommerce.order.service;

import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderSellerService {

  OrderProductDetailDto getOrderProductDetails(Long sellerId, Long id);
  Page<OrderProductDetailDto> getAllOrders(Long sellerId, Pageable pageable);
}
