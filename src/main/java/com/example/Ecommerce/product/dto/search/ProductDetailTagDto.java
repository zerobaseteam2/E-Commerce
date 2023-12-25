package com.example.Ecommerce.product.dto.search;

import com.example.Ecommerce.product.domain.ProductTag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailTagDto {

  @JsonProperty(value = "태그명")
  private String tagName;

  public static ProductDetailTagDto from(ProductTag productTag) {

    return ProductDetailTagDto.builder()
        .tagName(productTag.getTagName())
        .build();
  }

}
