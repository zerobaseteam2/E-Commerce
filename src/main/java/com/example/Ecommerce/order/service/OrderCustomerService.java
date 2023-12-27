package com.example.Ecommerce.order.service;

import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.UpdateQuantityDto;
import com.example.Ecommerce.order.dto.UpdateShippingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomerService {

  OrderDetailDto processOrder(String customerId, NewOrderDto newOrderDto);

  UpdateShippingDto.Response updateShippingInfo(Long orderId, UpdateShippingDto.Request request,
      String customerId);

  OrderDetailDto updateQuantity(UpdateQuantityDto updateQuantityDto, String customerId);

  OrderDetailDto getOrderDetails(String customerId, Long orderId);

  Page<OrderProductDetailDto> getAllOrders(Long customerId, Pageable pageable);
}
