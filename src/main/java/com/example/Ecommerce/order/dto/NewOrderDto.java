package com.example.Ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderDto {

  @NotBlank(message = "수령인은 필수 항목입니다.")
  private String recipientName;

  @NotBlank(message = "수령인 전화번호는 필수 항목입니다.")
  private String recipientPhone;

  @NotNull(message = "수령인 우편번호는 필수 항목입니다.")
  private Long zoneNo;

  @NotNull(message = "수령인 도로명주소는 필수 항목입니다.")
  private String roadAddress;

  @NotNull(message = "수령인 상세주소는 필수 항목입니다(동수, 호수 등 입력)")
  private String detailedAddress;

  private List<NewOrderProductDto> newOrderProductDtoList; // product id, option id, quantity

  private Long couponId; // 사용할 쿠폰 id

}
