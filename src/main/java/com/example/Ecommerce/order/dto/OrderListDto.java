package com.example.Ecommerce.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDto {

  private PageInfoDto pageInfoDto; //페이지 정보
  private List<OrderProductDto> orderProductDtos; //주문상품 리스트

  // page 정보와 주문상품 리스트를 반환
  public static OrderListDto of(Page<OrderProductDto> result) {

    PageInfoDto pageInfoDto = PageInfoDto.builder()
        .page(result.getNumber()) //해당 페이지
        .size(result.getSize()) //해당 페이지 상품수량
        .totalPage(result.getTotalPages()) //총페이지수
        .totalProducts(result.getTotalElements()) //총상품수량
        .build();

    return OrderListDto.builder()
        .pageInfoDto(pageInfoDto)
        .orderProductDtos(result.getContent()) //상품정보
        .build();
  }

}
