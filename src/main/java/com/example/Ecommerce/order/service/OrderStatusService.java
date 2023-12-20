package com.example.Ecommerce.order.service;

import com.example.Ecommerce.order.dto.UpdateStatusDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto.Request;
import com.example.Ecommerce.order.dto.UpdateStatusDto.Response;

public interface OrderStatusService {
  UpdateStatusDto.Response updateStatusByCustomer(String customerId, Request request);
}
