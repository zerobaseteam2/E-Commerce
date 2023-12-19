package com.example.Ecommerce.user.domain;

import com.example.Ecommerce.user.dto.UserAddressDto;
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
public class DeliveryAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private User user;

  @Column(nullable = false)
  private String roadAddress;

  @Column(nullable = false)
  private String detailAddress;

  @Column(nullable = false)
  private String zoneNo;

  @Column(nullable = false)
  private String addressName;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private boolean representAddress;

  public void modifyInfo(UserAddressDto.Request request) {
    this.roadAddress = request.getRoadAddress();
    this.detailAddress = request.getDetailAddress();
    this.zoneNo = request.getZoneNo();
    this.addressName = request.getAddressName();
    this.phone = request.getPhone();
  }
}
