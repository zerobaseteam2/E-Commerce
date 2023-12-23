package com.example.Ecommerce.coupon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Coupon")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long customerId;

  @Column(unique = true)
  private Long orderDetailNo;

  @Column(nullable = false)
  private String couponName;

  @Column
  private String usableCategory; // 상품 카테고리 확인 후 수정 필요

  @Column(nullable = false)
  private double discountRate;

  @CreatedDate
  @Column(nullable = false)
  private LocalDate issuanceDate;

  @Column(nullable = false)
  private LocalDate expirationDate;

  @Column(nullable = false)
  private boolean expires;

  public void useCoupon(Long orderDetailNo) {
    this.orderDetailNo = orderDetailNo;
  }

  public void couponExpires() {
    this.expires = true;
  }
}
