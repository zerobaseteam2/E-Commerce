package com.example.Ecommerce.product.dto.search;

import com.example.Ecommerce.product.domain.ProductOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailOptionDto {

  @JsonProperty(value = "옵션 명")
  private String optionName;

  @JsonProperty(value = "옵션 개수")
  private Integer count;

  public static ProductDetailOptionDto from(ProductOption productOption) {

    return ProductDetailOptionDto.builder()
        .optionName(productOption.getOptionName())
        .count(productOption.getCount())
        .build();
  }

}
