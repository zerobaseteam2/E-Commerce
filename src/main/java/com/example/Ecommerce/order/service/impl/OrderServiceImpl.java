package com.example.Ecommerce.order.service.impl;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.coupon.repository.CouponRepository;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.InvalidOrderStatusException;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderProductOption;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.OrderDetailDto;
import com.example.Ecommerce.order.dto.OrderProductDetailDto;
import com.example.Ecommerce.order.dto.UpdateQuantityDto;
import com.example.Ecommerce.order.dto.UpdateShippingDto;
import com.example.Ecommerce.order.repository.OrderProductOptionRepository;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.repository.OrderRepository;
import com.example.Ecommerce.order.service.OrderService;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.example.Ecommerce.product.dto.seller.ProductState;
import com.example.Ecommerce.product.repository.ProductOptionRepository;
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
  private final OrderProductOptionRepository orderProductOptionRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CouponRepository couponRepository;
  private final ProductOptionRepository productOptionRepository;


  @Override
  @Transactional
  public OrderDetailDto processOrder(String customerId, NewOrderDto newOrderDto) {

    //입력값으로 받은 customerId 로 찾은 회원
    Optional<User> user = userRepository.findByUserId(customerId);

    // 새로운 주문 생성
    Order order = Order.create(newOrderDto, user.get());
    orderRepository.save(order);

    // 상품 및 수량 정보로 주문상품 생성
    newOrderDto.getNewOrderProductDtoList().forEach(newOrderProductDto -> {
      // 입력된 주문 수량이 0개인 경우 exception 발생
      if (newOrderProductDto.getQuantity() <= 0) {
        throw new CustomException(ErrorCode.INVALID_QUANTITY);
      }
      // 상품 id 에 따른 상품 불러오기, id가 없어진 경우 exception 발생
      Product product = productRepository.findById(newOrderProductDto.getProductId())
          .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

      // 상품 상태 확인 (품절, 판매중지시 주문 불가)
      ProductState productState = product.getState();
      if (productState != null) {
        if (ProductState.STOP_SELLING.equals(productState) || ProductState.NO_STOCK.equals(
            productState)) {
          throw new InvalidOrderStatusException("주문이 불가능한 상품입니다. 상품 상태를 확인하세요: " + productState);
        }
      } else {
        throw new InvalidOrderStatusException("주문이 불가능한 상품입니다."); // 상품 상태가 존재하지 않으면
      }

      // 주문상품 생성 및 저장
      OrderProduct orderProduct = OrderProduct.builder()
          .order(order)
          .product(product)
          .status(OrderStatus.ORDER_COMPLETE)
          .build();

      // 상품 옵션
      Long productOptionId = newOrderProductDto.getOptionId();
      Integer quantity = newOrderProductDto.getQuantity();
      OrderProductOption orderProductOption;

      if (productOptionId != null) {
        // 상품 option id가 존재하면
        ProductOption productOption = productOptionRepository.findById(
                productOptionId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        // 재고 수정
        productOption.reduceInventory(quantity);
        // 주문상품 option 생성
        orderProductOption = OrderProductOption.createWithOptionId(productOptionId, quantity,
            orderProduct);
      } else {
        // 상품 option id가 존재하지 않으면 제외하고 주문상품 option 생성
        orderProductOption = OrderProductOption.create(quantity, orderProduct);
      }
      orderProductOptionRepository.save(orderProductOption);
      orderProductRepository.save(orderProduct);
      order.addOrderProduct(orderProduct); // 주문의 주문상품 리스트에 생성한 주문상품 저장
    });

    // 최초금액 계산
    order.calculateInitialTotalPrice();
    // 총할인금액 계산
    if (newOrderDto.getCouponId() != null) {
      Coupon coupon = couponRepository.findByIdAndCustomerId(newOrderDto.getCouponId(),
              user.get().getId())
          .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
      coupon.useCoupon(coupon, order.getId()); // 쿠폰 사용
      order.applyCouponDiscount(coupon); // 할인금액과 총결제금액 계산
    } else {
      order.calculateTotalPaymentPrice(); // 쿠폰 미사용시 총결제금액 계산
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
    // 수정하려는 주문상품 옵션 가져오기
    OrderProductOption orderProductOption = orderProductOptionRepository.findById(
            updateQuantityDto.getOrderProductOptionId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    // 권한 확인 - 수정하려는 주문정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!orderProductOption.getOrderProduct().getOrder().getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("수정하려는 주문에 접근할 권한이 없습니다.");
    }
    // 주문상품 상태 확인
    if (orderProductOption.getOrderProduct().getStatus() != OrderStatus.ORDER_COMPLETE) {
      throw new InvalidOrderStatusException("주문 수량 정보를 변경할수 없습니다. 주문 상태를 확인해 주세요.");
    }
    // 상품 수량 수정
    int quantity = updateQuantityDto.getQuantity();
    if (quantity == 0) {
      throw new CustomException(ErrorCode.INVALID_QUANTITY);
    }
    orderProductOption.updateQuantity(quantity);

    // 계산 다시하기
    refreshTotalPriceAfterQuantityUpdate(orderProductOption.getOrderProduct().getOrder());
    return OrderDetailDto.of(orderProductOption.getOrderProduct().getOrder());
  }

  // 상품수량 수정시 재계산
  public void refreshTotalPriceAfterQuantityUpdate(Order order) {
    // 해당 주문에 쿠폰 사용 내역이 존재한다면, 쿠폰 정보 가져와서 다시 계산
    if (order.getCouponId() != null) {
      Coupon coupon = couponRepository.findById(order.getCouponId())
          .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
      // 수정된 상품 수량으로 다시 계산
      order.recalculateTotalPrice(coupon);
    }

    // 해당 주문에 쿠폰 사용 내역이 없다면
    order.calculateInitialTotalPrice();
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
  public Page<OrderProductDetailDto> getAllOrders(Long customerId, Pageable pageable) {

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
    return orderProducts.map(OrderProductDetailDto::of);
  }


}
