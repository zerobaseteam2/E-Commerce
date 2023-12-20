package com.example.Ecommerce.order.service;

import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDto;
import com.example.Ecommerce.order.dto.UpdateQuantityDto;
import com.example.Ecommerce.order.dto.UpdateShippingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  OrderDetailDto processOrder(String userId, NewOrderDto newOrderDto);
  UpdateShippingDto.Response updateShippingInfo(Long id, UpdateShippingDto.Request request, String customerId);
  OrderDetailDto updateQuantity(UpdateQuantityDto updateQuantityDto, String customerId);
  OrderDetailDto getOrderDetails(String userId, Long id);
  Page<OrderProductDto> getAllOrders(Long customerId, Pageable pageable);
}
