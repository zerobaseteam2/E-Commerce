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
public class UpdateProductTagForm {

  private Long id;

  @NotBlank(message = "변경하실 태그명을 입력해주세여")
  private String tagName;

}
