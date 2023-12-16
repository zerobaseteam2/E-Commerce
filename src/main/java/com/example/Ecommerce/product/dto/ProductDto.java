package com.example.Ecommerce.product.dto;

import com.example.Ecommerce.product.domain.Product;
import java.util.List;
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
public class ProductDto {

  private Long id;

  private String name;
  private String category;
  private Integer price;
  private String description;
  private String origin;
  private ProductConfirm confirm;
  private ProductState discount;
  private ProductState state;
  private List<ProductOptionDto> productOptionList;
  private List<ProductTagDto> productTagDtoList;

  public static ProductDto from(Product product) {
    List<ProductOptionDto> productOptionDtoList = product.getProductOptionList()
        .stream().map(ProductOptionDto::from).toList();

    List<ProductTagDto> productTagDtoList = product.getProductTags()
        .stream().map(ProductTagDto::from).toList();

    return ProductDto.builder()
        .id(product.getId())
        .name(product.getName())
        .category(product.getCategory())
        .price(product.getPrice())
        .description(product.getDescription())
        .origin(product.getOrigin())
        .confirm(product.getConfirm())
        .discount(product.getDiscount())
        .state(product.getState())
        .productOptionList(productOptionDtoList)
        .productTagDtoList(productTagDtoList)
        .build();
  }
}
