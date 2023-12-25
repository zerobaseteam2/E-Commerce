package com.example.Ecommerce.product.dto.seller;


import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductOptionListDto {

  @JsonProperty(value = "상품명")
  private String name;

  @JsonProperty(value = "상품 승인 상태")
  private ProductConfirm confirm;

  @JsonProperty(value = "상품 옵션명")
  private String optionName;

  @JsonProperty(value = "상품 옵션 개수")
  private Integer count;

  public static ProductOptionListDto from(Product product, ProductOption option) {

    return ProductOptionListDto.builder()
        .name(product.getName())
        .confirm(product.getConfirm())
        .optionName(option.getOptionName())
        .count(option.getCount())
        .build();
  }

}
