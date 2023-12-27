package com.example.Ecommerce.inquiry.dto;

import com.example.Ecommerce.coupon.dto.PageResponse;
import com.example.Ecommerce.inquiry.domain.Inquiry;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryPageDto {
  private List<InquiryListDto> content;
  private int pageNo; // 현재 페이지 번호
  private int pageSize; // 한 페이지에 출력되는 데이터 개수
  private long totalElements; // 전체 데이터 개수
  private int totalPages; // 전체 페이지 수
  private boolean last; // 현재 페이지가 마지막 페이지일 경우 true

  public InquiryPageDto toDto(int pageNo, Page<Inquiry> page,
      List<InquiryListDto> responseDto) {
    return InquiryPageDto.builder()
        .content(responseDto)
        .pageNo(pageNo)
        .pageSize(10)
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .last(page.isLast())
        .build();
  }
}
