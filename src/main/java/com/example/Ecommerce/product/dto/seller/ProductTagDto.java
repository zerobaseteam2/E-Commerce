package com.example.Ecommerce.product.dto.seller;

import com.example.Ecommerce.product.domain.ProductTag;
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
public class ProductTagDto {

  private Long id;
  private String tagName;


  public static ProductTagDto from(ProductTag productTag) {
    return ProductTagDto.builder()
        .id(productTag.getId())
        .tagName(productTag.getTagName())
        .build();
  }
}
