package com.example.Ecommerce.order.controller;

import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.service.OrderService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

  private final OrderService orderService;

  // 주문 등록 API
  @PostMapping()
  public ResponseEntity<OrderDetailDto> createOrder(
      @RequestBody @Valid NewOrderDto newOrderDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();

    OrderDetailDto orderDetailDto = orderService.processOrder(customerId, newOrderDto);
    return ResponseEntity.ok(orderDetailDto);
  }


}
