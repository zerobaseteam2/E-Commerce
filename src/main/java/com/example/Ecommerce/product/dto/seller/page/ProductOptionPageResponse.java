package com.example.Ecommerce.product.dto.seller.page;


import com.example.Ecommerce.product.dto.seller.ProductOptionListDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOptionPageResponse {

  @JsonProperty(value = "상품 옵션 리스트")
  private List<ProductOptionListDto> productOptionList;

  private int pageNo;

  private int pageSize;

  private Long totalElements;

  private int totalPages;

  private boolean last;
}
