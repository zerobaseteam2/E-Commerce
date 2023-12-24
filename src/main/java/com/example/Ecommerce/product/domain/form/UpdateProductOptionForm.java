package com.example.Ecommerce.product.domain.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductOptionForm {

  private Long id;

  @NotBlank(message = "변경하실 상품의 옵션명을 입력해주세요")
  private String optionName;

  @Min(value = 0, message = "변경하실 수량은 0개보다 작을 수없습니다.")
  private Integer count;

}
