package com.example.Ecommerce.order.domain;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.order.dto.NewOrderDto;
import com.example.Ecommerce.order.dto.UpdateShippingDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "customer_order")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String recipientName; //수령인

  @Column(nullable = false)
  private String recipientPhone; //수령인 폰번호

  @Column(nullable = false)
  @CreatedDate
  private Date orderAt; //주문날짜

  @LastModifiedDate
  private Date updatedAt; //주문 수정날짜

  @Column(nullable = false)
  private Long zoneNo; //우편번호

  @Column(nullable = false)
  private String roadAddress; //도로명주소

  @Column(nullable = false)
  private String detailedAddress; //상세주소

  // 금액 관련
  @Column(nullable = false)
  private Integer initialTotalPrice; //최초계산금액
  @Column(nullable = false)
  private Integer totalDiscountPrice; //총할인금액
  @Column(nullable = false)
  private Integer totalPaymentPrice; //총결제금액

  // 쿠폰사용시
  @Column
  private Long couponId;

  // 회원(구매자)과의 관계
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private User user;

  // 주문상품과의 관계
  @OneToMany(mappedBy = "order")
  private List<OrderProduct> orderProductList = new ArrayList<>();

  // newOrderDto 로 주문 생성
  public static Order create(NewOrderDto newOrderDto, User user) {
    Order order = Order.builder()
        .recipientName(newOrderDto.getRecipientName())
        .recipientPhone(newOrderDto.getRecipientPhone())
        .zoneNo(newOrderDto.getZoneNo())
        .roadAddress(newOrderDto.getRoadAddress())
        .detailedAddress(newOrderDto.getDetailedAddress())
        .initialTotalPrice(0)
        .totalDiscountPrice(0)
        .totalPaymentPrice(0)
        .user(user)
        .orderProductList(new ArrayList<>())
        .build();

    return order;
  }

  // 주문 배송지 정보 수정
  public void updateShippingInfo(UpdateShippingDto.Request request) {
    if (request.getRecipientName() != null) {
      this.recipientName = request.getRecipientName();
    }
    if (request.getRecipientPhone() != null) {
      this.recipientPhone = request.getRecipientPhone();
    }
    if (request.getZoneNo() != null) {
      this.zoneNo = request.getZoneNo();
    }
    if (request.getRoadAddress() != null) {
      this.roadAddress = request.getRoadAddress();
    }
    if (request.getDetailedAddress() != null) {
      this.detailedAddress = request.getDetailedAddress();
    }
  }

  // 주문상품 리스트에 주문상품 추가
  public void addOrderProduct(OrderProduct orderProduct) {
    this.orderProductList.add(orderProduct);
  }

  // 최초 총금액 계산
  public void calculateInitialTotalPrice() {
    this.initialTotalPrice = orderProductList.stream()
        .flatMap(orderProduct -> orderProduct.getOrderProductOptionList().stream())
        .mapToInt(orderProductOption -> {
          Integer quantity = orderProductOption.getQuantity();
          Integer price = orderProductOption.getOrderProduct().getProduct().getPrice();

          return quantity * price;
        })
        .sum();
  }

  // 총할인금액 계산 - 최초 주문시 쿠폰이 있으면 적용
  public void applyCouponDiscount(Coupon coupon) {
    // 쿠폰 적용
    this.couponId = coupon.getId();
    // 할인금액 계산
    double discountedDouble = this.initialTotalPrice * coupon.getDiscountRate();
    // 할인금액 적용
    this.totalDiscountPrice = (int) Math.round(discountedDouble);
    // 할인금액을 제외한 총결제금액 다시 계산후 적용
    calculateTotalPaymentPrice();
  }

  // 총결제금액 계산
  public void calculateTotalPaymentPrice() {
    this.totalPaymentPrice = this.initialTotalPrice - this.totalDiscountPrice;
  }


  // 수량이 바뀌었을때 다시 총할인금액과 총결제금액 계산
  public void recalculateTotalPriceWithDiscount(Coupon coupon) {
    // 총금액 다시 계산
    calculateInitialTotalPrice();
    // 할인금액 계산
    double discountedDouble = this.initialTotalPrice * coupon.getDiscountRate();
    // 할인금액 적용
    this.totalDiscountPrice = (int) Math.round(discountedDouble);
    // 할인금액을 제외한 총결제금액 다시 계산후 적용
    calculateTotalPaymentPrice();
  }

}
