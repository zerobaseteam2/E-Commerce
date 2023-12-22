package com.example.Ecommerce.coupon.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

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
  private Long orderNo;
  
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
  
  public void useCoupon(Long orderNo) {
    this.orderNo = orderNo;
  }
  
  public void couponExpires() {
    this.expires = true;
  }
}
