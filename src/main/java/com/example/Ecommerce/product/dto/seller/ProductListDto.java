package com.example.Ecommerce.product.dto.seller;

import com.example.Ecommerce.product.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto {

  private Long id;

  @JsonProperty(value = "상품명")
  private String name;

  @JsonProperty(value = "카테고리")
  private String category;

  @JsonProperty(value = "상품 가격")
  private Integer price;

  @JsonProperty(value = "상품 설명")
  private String description;

  @JsonProperty(value = "상품 원산지")
  private String origin;

  @JsonProperty(value = "상품 승인 상태")
  private ProductConfirm confirm;

  @JsonProperty(value = "상품 할인 상태")
  private ProductState discount;

  @JsonProperty(value = "상품 상태")
  private ProductState state;

  @JsonProperty(value = "상품 별점")
  private Double stars;

  @JsonProperty(value = "거절사유")
  private String reason;

  public static ProductListDto from(Product product) {

    return ProductListDto.builder()
        .id(product.getId())
        .name(product.getName())
        .category(product.getCategory())
        .price(product.getPrice())
        .description(product.getDescription())
        .origin(product.getOrigin())
        .confirm(product.getConfirm())
        .discount(product.getDiscount())
        .state(product.getState())
        .reason(product.getReason())
        .build();
  }

  public static ProductListDto getList(Product product) {

    return ProductListDto.builder()
        .id(product.getId())
        .name(product.getName())
        .category(product.getCategory())
        .price(product.getPrice())
        .description(product.getDescription())
        .origin(product.getOrigin())
        .confirm(product.getConfirm())
        .stars(product.getStars())
        .discount(product.getDiscount())
        .state(product.getState())
        .reason(product.getReason())
        .build();
  }
}
