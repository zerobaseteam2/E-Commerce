package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UpdateShippingDto {


  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    // 수정할수 있는 배송 관련 정보
    private String recipientName;
    private String recipientPhone;
    private Long zoneNo;
    private String roadAddress;
    private String detailedAddress;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    // 수정된 배송 관련 정보
    private String recipientName;
    private String recipientPhone;
    private Long zoneNo;
    private String roadAddress;
    private String detailedAddress;


    // Order entity -> UpdateShipping dto
    public static Response of(Order order){
      return Response.builder()
          .recipientName(order.getRecipientName())
          .recipientPhone(order.getRecipientPhone())
          .zoneNo(order.getZoneNo())
          .roadAddress(order.getRoadAddress())
          .detailedAddress(order.getDetailedAddress())
          .build();
    }
  }
}
