package com.example.Ecommerce.order.domain;

import com.example.Ecommerce.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;
  @Column
  @Enumerated(value = EnumType.STRING)
  private OrderStatus status; //주문상태

  @OneToMany(mappedBy = "orderProduct")
  private List<OrderStatusHistory> statusHistoryList = new ArrayList<>();
  ;

  @OneToMany(mappedBy = "orderProduct")
  private List<OrderProductOption> OrderProductOptionList = new ArrayList<>();

  // 주문 상품 생성
  public static OrderProduct create(Order order, Product product) {
    return OrderProduct.builder()
        .order(order) //주문 정보
        .product(product) //상품 정보
        .status(OrderStatus.ORDER_COMPLETE) //상품 상태는 초기에 '주문완료'로 설정
        .OrderProductOptionList(new ArrayList<>()) //상품 옵션 리스트
        .build();
  }

  // 주문 상태 변경
  public void updateStatus(OrderStatus newStatus) {
    this.status = newStatus;
  }

  // 주문 상품 옵션 추가
  public void addOrderProductOption(OrderProductOption orderProductOption) {
    this.OrderProductOptionList.add(orderProductOption);
  }
}

