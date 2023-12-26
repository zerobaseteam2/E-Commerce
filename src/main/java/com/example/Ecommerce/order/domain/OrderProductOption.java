package com.example.Ecommerce.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Long productOptionId;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "order_product_id")
  private OrderProduct orderProduct;

  public static OrderProductOption createWithOptionId(Long productOptionId, Integer quantity, OrderProduct orderProduct) {
    return OrderProductOption.builder()
        .productOptionId(productOptionId)
        .quantity(quantity)
        .orderProduct(orderProduct)
        .build();
  }

  public static OrderProductOption create(Integer quantity, OrderProduct orderProduct) {
    return OrderProductOption.builder()
        .quantity(quantity)
        .orderProduct(orderProduct)
        .build();
  }


  // 주문 상품 수량 수정
  public void updateQuantity(int quantity) {
    this.quantity = quantity;
  }
}
