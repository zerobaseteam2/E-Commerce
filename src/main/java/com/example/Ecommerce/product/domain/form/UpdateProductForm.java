package com.example.Ecommerce.product.domain.form;

import com.example.Ecommerce.product.dto.seller.ProductState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductForm {

  private Long id;

  @NotBlank(message = "변경하실 상품명을 입력해주세요")
  private String name;

  @Min(value = 0, message = "변경하실 가격은 0보다 작은 숫자를 입력할수 없습니다.")
  private Integer price;

  @NotBlank(message = "변경하실 상품에 대한 설명을 입력해주세요")
  private String description;

  private String origin;

  @NotBlank(message = "변경하실 상품 할인 여부를 적어주세요")
  private ProductState discount;

  private List<UpdateProductOptionForm> updateProductOptionList = new ArrayList<>();

  private List<UpdateProductTagForm> updateTagList = new ArrayList<>();

}
