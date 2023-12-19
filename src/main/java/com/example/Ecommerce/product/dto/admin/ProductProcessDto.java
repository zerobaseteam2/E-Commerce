package com.example.Ecommerce.product.dto.admin;

import com.example.Ecommerce.product.dto.ProductConfirm;
import com.example.Ecommerce.product.dto.ProductState;
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
public class ProductProcessDto {

  private Long productId;
  private String name;
  private String category;
  private Integer price;
  private String description;
  private String origin;
  private ProductConfirm confirm;
  private ProductState discount;
  private ProductState state;
  private String reason;
  private Double stars;

}
