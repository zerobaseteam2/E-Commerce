package com.example.Ecommerce.order.service.impl;

import com.example.Ecommerce.exception.InvalidOrderStatusException;
import com.example.Ecommerce.exception.OrderNotFoundException;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.dto.UpdateStatusDto;
import com.example.Ecommerce.order.dto.UpdateStatusDto.Request;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

  private final OrderProductRepository orderProductRepository;

  @Override
  @Transactional
  public UpdateStatusDto.Response updateStatusByCustomer(String customerId, Request request) {

    // 주문상품 찾기
    OrderProduct orderProduct = orderProductRepository.findById(request.getOrderProductId())
        .orElseThrow(() -> new OrderNotFoundException("수정하려는 주문이 존재하지 않습니다."));

    // 권한 확인 - 수정하려는 주문정보의 구매자 정보와 로그인한 회원이 같은지 확인
    if (!orderProduct.getOrder().getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("해당 주문에 접근할 권한이 없습니다.");
    }
    // 주문 상태 변경
    processByCustomer(request, orderProduct);
    return UpdateStatusDto.Response.of(orderProduct);

  }

  private static void processByCustomer(Request request, OrderProduct orderProduct) {

    switch (request.getOrderStatus()) {
      // 취소 신청 - 주문 완료 상태일때만 변경 가능
      case ORDER_CANCELED:
        if (orderProduct.getStatus().equals(OrderStatus.ORDER_COMPLETE)) {
          orderProduct.updateStatus(request.getOrderStatus());
        } else {
          throw new InvalidOrderStatusException(
              "요청을 처리할 수 없습니다. 주문 상태: " + orderProduct.getStatus());
        }
        break;
      // 구매 화정 / 교환 신청 / 환불 신청 - 배송 완료 상태일때만 변경 가능
      case PURCHASE_CONFIRMED:
      case EXCHANGE_REQUESTED:
      case REFUND_REQUESTED:
        if (orderProduct.getStatus().equals(OrderStatus.SHIPPING_COMPLETE)) {
          orderProduct.updateStatus(request.getOrderStatus());
        } else {
          throw new InvalidOrderStatusException(
              "요청을 처리할 수 없습니다. 주문 상태: " + orderProduct.getStatus());
        }
        break;
      // 그 외 잘못된 요청 exception 발생
      default:
        throw new InvalidOrderStatusException("잘못된 주문 상태 변경 요청입니다.");
    }
  }


}
