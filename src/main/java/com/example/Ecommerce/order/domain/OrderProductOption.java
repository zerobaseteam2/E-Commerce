package com.example.Ecommerce.order.domain;

import com.example.Ecommerce.product.domain.ProductOption;
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
  private String productOptionName;
  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "order_product_id")
  private OrderProduct orderProduct;

  public static OrderProductOption create(ProductOption productOption, Integer quantity, OrderProduct orderProduct) {
    return OrderProductOption.builder()
        .productOptionId(productOption.getId()) //상품옵션아이디
        .productOptionName(productOption.getOptionName()) //상품옵션명
        .orderProduct(orderProduct) //주문 상품
        .quantity(quantity) //주문 수량
        .build();
  }



  // 주문 상품 수량 수정
  public void updateQuantity(int quantity) {
    this.quantity = quantity;
  }
}
