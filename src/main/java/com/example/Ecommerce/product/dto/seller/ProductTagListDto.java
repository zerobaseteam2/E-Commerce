package com.example.Ecommerce.product.dto.seller;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductTag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductTagListDto {

  @JsonProperty(value = "상품명")
  private String name;

  @JsonProperty(value = "상품 승인 상태")
  private ProductConfirm confirm;

  @JsonProperty(value = "상품 태그명")
  private String tagName;

  public static ProductTagListDto from(Product product, ProductTag productTag) {

    return ProductTagListDto.builder()
        .name(product.getName())
        .confirm(product.getConfirm())
        .tagName(productTag.getTagName())
        .build();
  }
}
