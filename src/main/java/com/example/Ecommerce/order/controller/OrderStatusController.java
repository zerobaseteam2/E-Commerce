package com.example.Ecommerce.order.controller;

import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.dto.list.OrderListDto;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.OrderStatusHistoryDto;
import com.example.Ecommerce.order.dto.list.StatusHistoryListDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto;
import com.example.Ecommerce.order.service.OrderStatusService;
import com.example.Ecommerce.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  // 판매자의 주문 상태 변경 API - 배송중/ 반품완료/ 교환완료/ 환불완료
  @PatchMapping("/seller")
  public ResponseEntity<?> updateStatusBySeller(
      @RequestBody UpdateStatusDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails){

    // 로그인한 회원 정보
    Long sellerId = userDetails.getUser().getId();

    UpdateStatusDto.Response response = orderStatusService.updateStatusBySeller(sellerId, request);
    return ResponseEntity.ok(response);
  }

  // 주문 상태 목록 조회 API
  @GetMapping("/list")
  public ResponseEntity<OrderListDto> getOrderByStatus(
      @RequestParam(name = "status") OrderStatus status,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetailsImpl userDetails){

    // 로그인한 회원 정보
    Long customerId = userDetails.getUser().getId();
    // paging 처리
    Pageable pageable = PageRequest.of(page, size);

    Page<OrderProductDetailDto> result = orderStatusService.getOrdersByStatus(customerId, status, pageable);
    return ResponseEntity.ok(OrderListDto.of(result));
  }

  // 주문 상품 상태 변경 정보(history) 조회 API
  @GetMapping("/history/list/{orderProductId}")
  public ResponseEntity<?> getOrderStatusHistory(
      @PathVariable Long orderProductId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetailsImpl userDetails){

    // 로그인한 회원 정보
    Long customerId = userDetails.getUser().getId();
    // paging 처리
    Pageable pageable = PageRequest.of(page, size);

    Page<OrderStatusHistoryDto> result = orderStatusService.getOrderStatusHistory(customerId, orderProductId, pageable);
    return ResponseEntity.ok(StatusHistoryListDto.of(result));
  }

}
