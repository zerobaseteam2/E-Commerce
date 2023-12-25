package com.example.Ecommerce.product.dto.seller.page;

import com.example.Ecommerce.product.dto.seller.ProductListDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPageResponse {

  private List<ProductListDto> productList;

  private int pageNo;

  private int pageSize;

  private Long totalElements;

  private int totalPages;

  private boolean last;
}
