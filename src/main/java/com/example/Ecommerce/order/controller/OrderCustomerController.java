package com.example.Ecommerce.order.controller;

import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.UpdateQuantityDto;
import com.example.Ecommerce.order.dto.UpdateShippingDto;
import com.example.Ecommerce.order.dto.list.OrderListDto;
import com.example.Ecommerce.order.service.OrderCustomerService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/order")
public class OrderCustomerController {

  private final OrderCustomerService orderCustomerService;

  // 주문 등록 API
  @PostMapping()
  public ResponseEntity<OrderDetailDto> createOrder(
      @RequestBody @Valid NewOrderDto newOrderDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();

    OrderDetailDto orderDetailDto = orderCustomerService.processOrder(customerId, newOrderDto);
    return ResponseEntity.ok(orderDetailDto);
  }

  // 주문 배송지 정보 수정 API
  @PutMapping("/{orderId}/shipping-info")
  public ResponseEntity<UpdateShippingDto.Response> updateShippingInfo(
      @PathVariable Long orderId,
      @RequestBody UpdateShippingDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();

    UpdateShippingDto.Response response = orderCustomerService.updateShippingInfo(orderId, request, customerId);
    return ResponseEntity.ok(response);
  }

  // 주문 상품 수량 수정 API
  @PutMapping("/quantity-info")
  public ResponseEntity<OrderDetailDto> updateQuantity(
      @RequestBody @Valid UpdateQuantityDto updateQuantityDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();

    OrderDetailDto orderDetailDto = orderCustomerService.updateQuantity(updateQuantityDto, customerId);
    return ResponseEntity.ok(orderDetailDto);
  }

  // 주문 상세 내역 조회 API
  @GetMapping("/{orderId}/details")
  public ResponseEntity<OrderDetailDto> getOrderDetails(
      @PathVariable Long orderId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();

    OrderDetailDto orderDetailDto = orderCustomerService.getOrderDetails(customerId, orderId);
    return ResponseEntity.ok(orderDetailDto);
  }


  // 주문 목록 조회 API
  @GetMapping("/list")
  public ResponseEntity<OrderListDto> getAllOrders(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    Long customerId = userDetails.getUser().getId();
    // paging 처리
    Pageable pageable = PageRequest.of(page, size);

    Page<OrderProductDetailDto> result = orderCustomerService.getAllOrders(customerId, pageable);
    return ResponseEntity.ok(OrderListDto.of(result));
  }
}
