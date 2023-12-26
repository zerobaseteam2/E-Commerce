package com.example.Ecommerce.product.dto.search;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPageResponse {

  private List<SearchDto> searchDtoList;
  private int pageNo;
  private int pageSize;
  private Long totalElements;
  private int totalPages;
  private boolean last;

}
