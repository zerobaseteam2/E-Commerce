package com.example.Ecommerce.order.domain;

import com.example.Ecommerce.order.dto.NewOrderDto;
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
import java.time.LocalDate;
import java.util.ArrayList;
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
  private LocalDate orderAt; //주문날짜

  @LastModifiedDate
  private LocalDate updatedAt; //주문 수정날짜

  @Column(nullable = false)
  private Long zoneNo; //우편번호

  @Column(nullable = false)
  private String roadAddress; //도로명주소

  @Column(nullable = false)
  private String detailedAddress; //상세주소

  private int totalDiscountPrice; //총할인금액
  @Column(nullable = false)
  private int totalPaymentPrice; //총결제금액

  //mapping (쿠폰, 회원, 주문상품)
  // 회원(구매자)과의 관계
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private User user;

  // 주문상품과의 관계
  @OneToMany(mappedBy = "order")
  private List<OrderProduct> orderProductList;


  // newOrderDto 로 주문 생성
  public static Order createOrder(NewOrderDto newOrderDto, User user) {
    Order order = Order.builder()
        .recipientName(newOrderDto.getRecipientName())
        .recipientPhone(newOrderDto.getRecipientPhone())
        .zoneNo(newOrderDto.getZoneNo())
        .roadAddress(newOrderDto.getRoadAddress())
        .detailedAddress(newOrderDto.getDetailedAddress())
        .user(user)
        .orderProductList(new ArrayList<>())
        .build();

    return order;
  }


  // 주문상품 리스트에 주문상품 추가
  public void addOrderProduct(OrderProduct orderProduct) {
    this.orderProductList.add(orderProduct);
  }

  // 쿠폰


  // 총결제금액 계산
  public void calculateTotalPaymentPrice() {
    int totalPaymentPrice = orderProductList.stream()
        .mapToInt(orderProduct -> orderProduct.getQuantity() * orderProduct.getProduct().getPrice())
        .sum();
    this.totalPaymentPrice = totalPaymentPrice;
  }

}
