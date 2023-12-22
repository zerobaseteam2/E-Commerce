package com.example.Ecommerce.user.dto;

import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class UserAddressDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank(message = "도로명 주소는 필수 입력값입니다.")
    private String roadAddress;

    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String detailAddress;

    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    private String zoneNo;

    @NotBlank(message = "배송지이름은 필수 입력값입니다.")
    private String addressName;

    @NotBlank(message = "수신자 번호는 필수 입력값입니다.")
    private String phone;

    public DeliveryAddress toEntity(User user, boolean existsRepresentAddress) {
      if (existsRepresentAddress) {
        return DeliveryAddress.builder()
            .user(user)
            .roadAddress(roadAddress)
            .detailAddress(detailAddress)
            .zoneNo(zoneNo)
            .addressName(addressName)
            .phone(phone)
            .isRepresentAddress(false)
            .build();
      }

      return DeliveryAddress.builder()
          .user(user)
          .roadAddress(roadAddress)
          .detailAddress(detailAddress)
          .zoneNo(zoneNo)
          .addressName(addressName)
          .phone(phone)
          .isRepresentAddress(true)
          .build();
    }
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long addressId;

    private String roadAddress;

    private String detailAddress;

    private String zoneNo;

    private String addressName;

    private String phone;

    public static UserAddressDto.Response fromEntity(DeliveryAddress deliveryAddress) {
      return Response.builder()
          .addressId(deliveryAddress.getId())
          .roadAddress(deliveryAddress.getRoadAddress())
          .detailAddress(deliveryAddress.getDetailAddress())
          .zoneNo(deliveryAddress.getZoneNo())
          .addressName(deliveryAddress.getAddressName())
          .phone(deliveryAddress.getPhone())
          .build();
    }
  }
}
