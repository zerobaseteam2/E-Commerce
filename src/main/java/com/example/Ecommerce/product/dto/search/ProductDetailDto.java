package com.example.Ecommerce.product.dto.search;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.dto.seller.ProductState;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {

  @JsonProperty(value = "상품명")
  private String name;

  @JsonProperty(value = "상품 카테고리")
  private String category;

  @JsonProperty(value = "상품 가격")
  private Integer price;

  @JsonProperty(value = "상품 설명")
  private String description;

  @JsonProperty(value = "상품 원산지")
  private String origin;

  @JsonProperty(value = "상품 할인 여부")
  private ProductState discount;

  @JsonProperty(value = "상품 별점")
  private Double stars;

  @JsonProperty(value = "상품 리뷰 개수")
  private Long reviewCount;

  @JsonProperty(value = "상품 옵션")
  private List<ProductDetailOptionDto> productOptionList;

  @JsonProperty(value = "상품 태그")
  private List<ProductDetailTagDto> productTagDtoList;

  public static ProductDetailDto from(Product product, Long reviewCount) {

    List<ProductDetailOptionDto> productDetailOptionList = product.getProductOptionList().stream()
        .map(ProductDetailOptionDto::from).toList();

    List<ProductDetailTagDto> productDetailTagList = product.getProductTags().stream()
        .map(ProductDetailTagDto::from).toList();

    return ProductDetailDto.builder()
        .name(product.getName())
        .category(product.getCategory())
        .price(product.getPrice())
        .description(product.getDescription())
        .origin(product.getOrigin())
        .discount(product.getDiscount())
        .stars(product.getStars())
        .productOptionList(productDetailOptionList)
        .productTagDtoList(productDetailTagList)
        .reviewCount(reviewCount)
        .build();
  }

}
