package com.example.Ecommerce.order.service.impl;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.service.OrderSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSellerServiceImpl implements OrderSellerService {

  private final OrderProductRepository orderProductRepository;

  @Override
  public OrderProductDetailDto getOrderProductDetails(Long sellerId, Long id) {
    // 조회하려는 주문 가져오기
    OrderProduct orderProduct = orderProductRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));

    // 권한 확인 - 조회하려는 주문정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!orderProduct.getProduct().getSellerId().equals(sellerId)) {
      throw new UnauthorizedUserException("해당 주문 상세 내역에 접근할 권한이 없습니다.");
    }
    return OrderProductDetailDto.of(orderProduct);
  }

  @Override
  public Page<OrderProductDetailDto> getAllOrders(Long sellerId, Pageable pageable) {
    Page<OrderProduct> orderProducts
        = orderProductRepository.findAllByProductSellerId(sellerId, pageable);

    // 주문 목록이 없으면 exception 발생
    if (orderProducts.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_PRODUCT_NOT_FOUND);
    }
    // 결과를 OrderProductDto list 로 반환하여 반환
    return orderProducts.map(OrderProductDetailDto::of);
  }
}
