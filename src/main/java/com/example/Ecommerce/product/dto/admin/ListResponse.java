package com.example.Ecommerce.product.dto.admin;

import com.example.Ecommerce.product.dto.seller.ProductDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListResponse {

  private List<ProductDto> productList;
  private int pageNo;
  private int pageSize;
  private Long totalElements;
  private int totalPages;
  private boolean last;

}
