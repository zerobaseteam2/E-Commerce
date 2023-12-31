package com.example.Ecommerce.order.service.impl;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.InvalidOrderStatusException;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.domain.OrderStatusHistory;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.OrderStatusHistoryDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto.Request;
import com.example.Ecommerce.order.dto.UpdateStatusDto.Response;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.repository.OrderStatusHistoryRepository;
import com.example.Ecommerce.order.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

  private final OrderProductRepository orderProductRepository;

  private final OrderStatusHistoryRepository orderStatusHistoryRepository;

  @Override
  @Transactional
  public UpdateStatusDto.Response updateStatusByCustomer(String customerId, Request request) {

    // 주문상품 찾기
    OrderProduct orderProduct = orderProductRepository.findById(request.getOrderProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

    // 권한 확인 - 수정하려는 주문정보의 구매자 정보와 로그인한 회원이 같은지 확인
    if (!orderProduct.getOrder().getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("해당 주문에 접근할 권한이 없습니다.");
    }
    // 이전 상태
    OrderStatus previousStatus = orderProduct.getStatus();
    // 주문 상태 변경
    processByCustomer(request, orderProduct);
    // 주문 상태 변경 history
    saveOrderStatusHistory(orderProduct, previousStatus);
    return UpdateStatusDto.Response.of(orderProduct);

  }


  private void saveOrderStatusHistory(OrderProduct orderProduct, OrderStatus previousStatus) {
    OrderStatusHistory history = OrderStatusHistory.createHistory(
        orderProduct, previousStatus, orderProduct.getStatus()
    );
    orderStatusHistoryRepository.save(history);
  }

  @Override
  @Transactional
  public Response updateStatusBySeller(Long sellerId, Request request) {
    // 주문상품 찾기
    OrderProduct orderProduct = orderProductRepository.findById(request.getOrderProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

    // 권한 확인 - 수정하려는 주문정보의 구매자 정보와 로그인한 회원이 같은지 확인
    if (orderProduct.getProduct().getSellerId() != sellerId) {
      throw new UnauthorizedUserException("해당 주문에 접근할 권한이 없습니다.");
    }
    // 이전 상태
    OrderStatus previousStatus = orderProduct.getStatus();
    // 주문 상태 변경
    processBySeller(request, orderProduct);
    // 주문 상태 변경 history
    saveOrderStatusHistory(orderProduct, previousStatus);
    return UpdateStatusDto.Response.of(orderProduct);
  }

  public Page<OrderProductDetailDto> getOrdersByStatus(Long sellerId, OrderStatus status,
      Pageable pageable) {

    Page<OrderProduct> orderProducts
        = orderProductRepository.findAllByProductSellerIdAndStatus(sellerId, status, pageable);

    // 주문 목록이 없으면 exception 발생
    if (orderProducts.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUND);
    }
    // 결과를 OrderProductDto list 로 반환하여 반환
    return orderProducts.map(OrderProductDetailDto::of);
  }

  @Override
  public Page<OrderStatusHistoryDto> getOrderStatusHistory(Long customerId, Long orderProductId, Pageable pageable) {

    OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
        .orElseThrow(()-> new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

    Page<OrderStatusHistory> orderStatusHistories =
        orderStatusHistoryRepository.findAllByOrderProduct_Id(orderProductId, pageable);
    // 결과를 orderStatusHistoryDto list 로 반환하여 반환
    return orderStatusHistories.map(OrderStatusHistoryDto::of);
  }


  private static void processByCustomer(Request request, OrderProduct orderProduct) {

    switch (request.getOrderStatus()) {
      // 취소 신청으로 변경
      case ORDER_CANCELED:
        // 주문완료인 상태만 변경 가능
        validateStatusUpdate(orderProduct, OrderStatus.ORDER_COMPLETE);
        orderProduct.updateStatus(request.getOrderStatus());
        break;
      // 구매 화정 / 교환 신청 / 환불 신청으로 변경
      case PURCHASE_CONFIRMED:
      case EXCHANGE_REQUESTED:
      case REFUND_REQUESTED:
        //배송 완료 상태일때만 변경 가능
        validateStatusUpdate(orderProduct, OrderStatus.SHIPPING_COMPLETE);
        orderProduct.updateStatus(request.getOrderStatus());
        break;
      // 그 외 잘못된 요청 exception 발생
      default:
        throw new InvalidOrderStatusException("잘못된 주문 상태 변경 요청입니다.");
    }
  }

  private void processBySeller(Request request, OrderProduct orderProduct) {

    switch (request.getOrderStatus()) {
      // 배송중으로 변경
      case SHIPPING:
        // 주문 완료 상태일때만 변경 가능
        validateStatusUpdate(orderProduct, OrderStatus.ORDER_COMPLETE);
        orderProduct.updateStatus(request.getOrderStatus());
        break;
      // 반품 완료으로 변경
      case RETURN_COMPLETE:
        // 교환 신청, 반품 신청 상태일때만 변경 가능
        validateReturnComplete(orderProduct);
        orderProduct.updateStatus(request.getOrderStatus());
        break;
      // 교환 완료 / 환불완료으로 변경
      case EXCHANGE_COMPLETE:
      case REFUND_COMPLETE:
        // 반품 완료 상태일때만 변경 가능
        validateStatusUpdate(orderProduct, OrderStatus.RETURN_COMPLETE);
        orderProduct.updateStatus(request.getOrderStatus());
        break;
      // 그 외 잘못된 요청 exception 발생
      default:
        throw new InvalidOrderStatusException("잘못된 주문 상태 변경 요청입니다.");
    }
  }

  private static void validateStatusUpdate(OrderProduct orderProduct, OrderStatus allowedStatus) {
    // 현재 주문 상태가 변경 가능한 상태인지 확인
    if (!orderProduct.getStatus().equals(allowedStatus)) {
      throw new InvalidOrderStatusException(
          "요청을 처리할 수 없습니다. 현재 주문 상태: " + orderProduct.getStatus());
    }
  }

  private void validateReturnComplete(OrderProduct orderProduct) {
    // 현재 주문 상태가 반품 완료로 변경 가능한 상태인지 확인
    if (!(orderProduct.getStatus().equals(OrderStatus.EXCHANGE_REQUESTED)
        || orderProduct.getStatus().equals(OrderStatus.REFUND_REQUESTED))) {
      throw new InvalidOrderStatusException(
          "반품 완료를 처리할 수 없습니다. 현재 주문 상태: " + orderProduct.getStatus());
    }
  }


}
