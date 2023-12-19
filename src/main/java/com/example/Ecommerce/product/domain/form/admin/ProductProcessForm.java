package com.example.Ecommerce.product.domain.form.admin;

import com.example.Ecommerce.product.dto.ProductConfirm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProcessForm {

  private Long productId;

  private ProductConfirm confirm;

  private String reason;

}
