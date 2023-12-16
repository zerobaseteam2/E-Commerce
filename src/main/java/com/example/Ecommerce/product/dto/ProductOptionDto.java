package com.example.Ecommerce.product.dto;

import com.example.Ecommerce.product.domain.ProductOption;
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
public class ProductOptionDto {

  private Long id;
  private String optionName;
  private Integer count;

  public static ProductOptionDto from(ProductOption productOption) {
    return ProductOptionDto.builder()
        .id(productOption.getId())
        .optionName(productOption.getOptionName())
        .count(productOption.getCount())
        .build();
  }
}
