package com.example.Ecommerce.order.service;

import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;

public interface OrderService {

  OrderDetailDto processOrder(String userId, NewOrderDto newOrderDto);
}
