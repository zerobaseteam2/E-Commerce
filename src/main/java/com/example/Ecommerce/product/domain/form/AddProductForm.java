package com.example.Ecommerce.product.domain.form;

import com.example.Ecommerce.product.dto.ProductState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class AddProductForm {

  @NotBlank(message = "카테고리를 설정해주세요")
  private String category;

  @NotBlank(message = "상품명을 입력해주세요")
  private String name;

  @Min(value = 0, message = "가격은 0보다 작은 숫자를 입력할수 없습니다.")
  private Integer price;

  @NotBlank(message = "상품에 대한 설명을 입력해주세요")
  private String description;

  private String origin;

  private ProductState discount;

  @Size(min = 1, message = "최소한개이상의 옵션을 등록해주세요")
  private List<AddProductOptionForm> optionFormList = new ArrayList<>();

  @Size(min = 1, message = "최소한개이상의 태그를 등록해주세요")
  private List<AddProductTagForm> tagList = new ArrayList<>();

}
