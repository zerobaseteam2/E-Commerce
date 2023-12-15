package com.example.Ecommerce.product.domain.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductTagForm {

  private Long productId;

  @NotBlank(message = "태그명을 입력해주세요")
  private String tagName;

}
