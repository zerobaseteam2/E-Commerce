package com.example.Ecommerce.order.service.impl;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.coupon.repository.CouponRepository;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.InvalidOrderStatusException;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDto;
import com.example.Ecommerce.order.dto.UpdateQuantityDto;
import com.example.Ecommerce.order.dto.UpdateShippingDto;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.repository.OrderRepository;
import com.example.Ecommerce.order.service.OrderService;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.repository.ProductRepository;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CouponRepository couponRepository;


  @Override
  @Transactional
  public OrderDetailDto processOrder(String customerId, NewOrderDto newOrderDto) {

    //입력값으로 받은 customerId 로 찾은 회원
    Optional<User> user = userRepository.findByUserId(customerId);

    // 새로운 주문 생성
    Order order = Order.create(newOrderDto, user.get());
    orderRepository.save(order);

    // 상품 및 수량 정보로 주문상품 생성
    newOrderDto.getProductQuantityMap().forEach((productId, quantity) -> {
      // 상품 id 에 따른 상품 불러오기, id가 없어진 경우 exception 발생
      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
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

    // 총금액 계산
    order.calculateTotalPrice();

    // 총할인금액 계산
    // 쿠폰이 있는경우만 사용
    if (newOrderDto.getCouponId() != null){
      Coupon coupon  = couponRepository.findByIdAndCustomerId(newOrderDto.getCouponId(), user.get().getId())
          .orElseThrow(()-> new CustomException(ErrorCode.COUPON_NOT_FOUND));
      // 쿠폰 사용
      coupon.useCoupon(coupon, order.getId());
      // 할인금액과 총금액 계산
      order.calculateTotalDiscountPrice(coupon);
    }
    // 주문 저장
    orderRepository.save(order);

    //생성한 주문의 상세내역 반환
    return OrderDetailDto.of(order);
  }

  @Override
  @Transactional
  public UpdateShippingDto.Response updateShippingInfo
      (Long id, UpdateShippingDto.Request request, String customerId) {
    // 수정하려는 주문 가져오기
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    // 권한 확인 - 수정하려는 주문정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!order.getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("수정하려는 주문에 접근할 권한이 없습니다.");
    }

    // 상태 확인
    for (OrderProduct orderProduct : order.getOrderProductList()) {
      if (orderProduct.getStatus() != OrderStatus.ORDER_COMPLETE) {
        throw new InvalidOrderStatusException("주문 배송지 정보를 변경할수 없습니다. 주문 상태를 확인해 주세요.");
      }
    }
    // 수정
    order.updateShippingInfo(request);
    return UpdateShippingDto.Response.of(order);
  }

  @Override
  @Transactional
  public OrderDetailDto updateQuantity
      (UpdateQuantityDto updateQuantityDto, String customerId) {
    // 수정하려는 주문상품 가져오기
    OrderProduct orderProduct = orderProductRepository.findById(
            updateQuantityDto.getOrderProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    // 권한 확인 - 수정하려는 주문정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!orderProduct.getOrder().getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("수정하려는 주문에 접근할 권한이 없습니다.");
    }
    // 주문상품 상태 확인
    if (orderProduct.getStatus() != OrderStatus.ORDER_COMPLETE) {
      throw new InvalidOrderStatusException("주문 수량 정보를 변경할수 없습니다. 주문 상태를 확인해 주세요.");
    }
    // 상품 수량 수정
    int quantity = updateQuantityDto.getQuantity();
    if (quantity == 0) {
      throw new CustomException(ErrorCode.INVALID_QUANTITY);
    }
    orderProduct.updateQuantity(quantity);

    // 계산 다시하기
    recalculateOrderTotalPrice(orderProduct.getOrder());
    return OrderDetailDto.of(orderProduct.getOrder());
  }

  // 상품수량 수정시 재계산
  public void recalculateOrderTotalPrice(Order order) {
    // 해당 주문에 쿠폰 사용 내역이 존재한다면, 쿠폰 정보 가져와서 다시 계산
    if (order.getCouponId() != null) {
      Coupon coupon = couponRepository.findById(order.getCouponId())
          .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
      // 수정된 상품 수량으로 다시 계산
      order.recalculateTotalDiscountPrice(coupon);
    }

    // 해당 주문에 쿠폰 사용 내역이 없다면
    order.calculateTotalPrice();
  }

  @Override
  public OrderDetailDto getOrderDetails(String customerId, Long id) {

    // 조회하려는 주문 가져오기
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    // 권한 확인 - 조회하려는 주문정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!order.getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("해당 주문 상세 내역에 접근할 권한이 없습니다.");
    }
    return OrderDetailDto.of(order);
  }


  @Override
  public Page<OrderProductDto> getAllOrders(Long customerId, Pageable pageable) {

    // 로그인한 회원의 주문정보
    List<Order> orderList = orderRepository.findAllByUser(userRepository.findById(customerId));

    // 해당 회원의 주문이 없는 경우 exception 발생
    if (orderList.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
    }
    // 주문 id 리스트
    List<Long> orderIds = orderList.stream()
        .map(Order::getId)
        .collect(Collectors.toList());

    // 주문상품 목록 가져오기
    Page<OrderProduct> orderProducts = orderProductRepository.findAllByOrderIdIn(orderIds,
        pageable);

    // 결과를 OrderDto list 로 반환
    return orderProducts.map(OrderProductDto::of);
  }


}
