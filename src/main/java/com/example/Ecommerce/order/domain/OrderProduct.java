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
  private List<OrderStatusHistory> statusHistoryList;

  @OneToMany(mappedBy = "orderProduct")
  private List<OrderProductOption> OrderProductOptionList;


  // 주문 상태 변경
  public void updateStatus(OrderStatus newStatus) {
    this.status = newStatus;
  }

  public void addOrderProductOption(OrderProductOption orderProductOption) {
    this.OrderProductOptionList.add(orderProductOption);
  }
}

