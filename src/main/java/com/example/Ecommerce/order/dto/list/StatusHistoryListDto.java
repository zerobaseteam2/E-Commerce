package com.example.Ecommerce.order.dto.list;

import com.example.Ecommerce.order.dto.OrderStatusHistoryDto;
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
public class StatusHistoryListDto {
  private PageInfoDto pageInfoDto; //페이지 정보
  private List<OrderStatusHistoryDto> historyDtos; //주문상품 리스트

  // page 정보와 주문상품 리스트를 반환
  public static StatusHistoryListDto of(Page<OrderStatusHistoryDto> result) {

    PageInfoDto pageInfoDto = PageInfoDto.builder()
        .page(result.getNumber()) //해당 페이지
        .size(result.getSize()) //해당 페이지 상품수량
        .totalPage(result.getTotalPages()) //총페이지수
        .totalProducts(result.getTotalElements()) //총상품수량
        .build();

    return StatusHistoryListDto.builder()
        .pageInfoDto(pageInfoDto)
        .historyDtos(result.getContent())
        .build();
  }
}
