package com.example.Ecommerce.coupon.domain;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
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
  private Long orderNo;

  @Column(nullable = false)
  private String couponName;

  @Column(nullable = false)
  private double discountRate;

  @CreatedDate
  @Column(nullable = false)
  private LocalDate issuanceDate;

  @Column(nullable = false)
  private LocalDate expirationDate;

  @Column(nullable = false)
  private boolean isExpired;


  public void useCoupon(Coupon coupon, Long orderNo) {
    // 만료된 쿠폰인지 확인
    if (coupon.isExpired) {
      throw new CustomException(ErrorCode.COUPON_IS_EXPIRED);
    }
    // 사용된 쿠폰인지 확인
    if (coupon.orderNo != null) {
      throw new CustomException(ErrorCode.USED_COUPON);
    }
    // 쿠폰 사용
    this.orderNo = orderNo;
  }

  public void couponExpires() {
    this.isExpired = true;
  }
}
