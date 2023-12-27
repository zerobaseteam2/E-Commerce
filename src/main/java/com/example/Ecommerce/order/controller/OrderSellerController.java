package com.example.Ecommerce.order.controller;

import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.list.OrderListDto;
import com.example.Ecommerce.order.service.OrderSellerService;
import com.example.Ecommerce.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/order")
public class OrderSellerController {

  private final OrderSellerService orderSellerService;

  // 주문 상품 상세 내역 조회 API
  @GetMapping("/{id}/details")
  public ResponseEntity<OrderProductDetailDto> getOrderDetails(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    Long sellerId = userDetails.getUser().getId();

    OrderProductDetailDto orderProductDetailDto = orderSellerService.getOrderProductDetails(sellerId, id);
    return ResponseEntity.ok(orderProductDetailDto);
  }


  // 주문 목록 조회 API
  @GetMapping("/list")
  public ResponseEntity<OrderListDto> getAllOrders(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    Long sellerId = userDetails.getUser().getId();
    // paging 처리
    Pageable pageable = PageRequest.of(page, size);

    Page<OrderProductDetailDto> result = orderSellerService.getAllOrders(sellerId, pageable);
    return ResponseEntity.ok(OrderListDto.of(result));
  }
}
