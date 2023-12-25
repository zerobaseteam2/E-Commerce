package com.example.Ecommerce.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderStatusHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Enumerated(value = EnumType.STRING)
  private OrderStatus previousStatus; //이전 상태

  @Column
  @Enumerated(value = EnumType.STRING)
  private OrderStatus currentStatus; //변경 상태

  @LastModifiedDate
  private LocalDate updatedAt; //수정날짜

  @ManyToOne
  @JoinColumn(name = "order_product_id", nullable = false)
  private OrderProduct orderProduct;

  public static OrderStatusHistory createHistory(OrderProduct orderProduct, OrderStatus previousStatus,
      OrderStatus currentStatus) {

    return OrderStatusHistory.builder()
        .previousStatus(previousStatus)
        .currentStatus(currentStatus)
        .orderProduct(orderProduct)
        .build();
  }
}
