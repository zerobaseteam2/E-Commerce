package com.example.Ecommerce.order.service;

import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.OrderStatusHistoryDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderStatusService {
  UpdateStatusDto.Response updateStatusByCustomer(String customerId, Request request);
  UpdateStatusDto.Response updateStatusBySeller(Long sellerId, Request request);
  Page<OrderProductDetailDto> getOrdersByStatus(Long customerId, OrderStatus status, Pageable pageable);
  Page<OrderStatusHistoryDto> getOrderStatusHistory(Long customerId, Long id, Pageable pageable);
}
