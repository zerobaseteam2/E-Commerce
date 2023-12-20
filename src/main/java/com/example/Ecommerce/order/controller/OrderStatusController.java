package com.example.Ecommerce.order.controller;

import com.example.Ecommerce.order.dto.UpdateStatusDto;
import com.example.Ecommerce.order.service.OrderStatusService;
import com.example.Ecommerce.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-status")
public class OrderStatusController {

  private final OrderStatusService orderStatusService;

  // 구매자의 주문 상태 변경 API - 취소완료/ 구매확정/ 교환신청/ 환불신청
  @PatchMapping("/customer")
  public ResponseEntity<?> updateStatusByCustomer(
      @RequestBody UpdateStatusDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails){

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();

    UpdateStatusDto.Response response = orderStatusService.updateStatusByCustomer(customerId, request);
    return ResponseEntity.ok(response);
  }



}
