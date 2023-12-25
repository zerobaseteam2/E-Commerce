package com.example.Ecommerce.product.dto.search;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.dto.seller.ProductState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class SearchDto {

  @JsonProperty(value = "상품 이름")
  private String name;

  @JsonProperty(value = "카테고리명")
  private String category;

  @JsonProperty(value = "상품 가격")
  private Integer price;

  @JsonProperty(value = "상품 할인여부")
  private ProductState discount;

  @JsonProperty(value = "별점")
  private Double stars;

  @JsonProperty(value = "리뷰 개수")
  private Long reviewCount;


  public static SearchDto from(Product product, Long reviewCount) {

    return SearchDto.builder()
        .name(product.getName())
        .category(product.getCategory())
        .price(product.getPrice())
        .discount(product.getDiscount())
        .stars(product.getStars())
        .reviewCount(reviewCount)
        .build();
  }
}
