package com.example.Ecommerce.order.service.impl;

import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.repository.OrderRepository;
import com.example.Ecommerce.order.service.OrderService;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.repository.ProductRepository;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public OrderDetailDto processOrder(String customerId, NewOrderDto newOrderDto) {

    //입력값으로 받은 customerId 로 찾은 회원
    Optional<User> user = userRepository.findByUserId(customerId);

    // 새로운 주문 생성
    Order order = Order.createOrder(newOrderDto, user.get());
    orderRepository.save(order);

    // 상품 및 수량 정보로 주문상품 생성
    newOrderDto.getProductQuantityMap().forEach((productId, quantity) -> {
      // 상품 id 에 따른 상품 불러오기, id가 없어진 경우 exception 발생
      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new RuntimeException("다음 상품이 존재하지 않습니다: " + productId));

      // 주문상품 생성 및 저장
      OrderProduct orderProduct = OrderProduct.builder()
          .order(order)
          .product(product)
          .quantity(quantity)
          .status(OrderStatus.ORDER_COMPLETE) //초기상태 = 주문완료
          .build();

      orderProductRepository.save(orderProduct);
      // 주문의 주문상품 리스트에 생성한 주문상품 저장
      order.addOrderProduct(orderProduct);
    });

    // 총할인금액 계산 - 쿠폰 확인하기

    // 총결제금액 계산
    order.calculateTotalPaymentPrice();

    // 주문 저장
    orderRepository.save(order);

    //생성한 주문의 상세내역 반환
    return OrderDetailDto.of(order);
  }



}
