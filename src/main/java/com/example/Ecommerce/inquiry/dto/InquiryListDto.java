package com.example.Ecommerce.inquiry.dto;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.user.domain.UserRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryListDto {
  private String userId;
  private UserRole userRole;
  private String title;
  private boolean state;
  private LocalDate createdAt;

  public static InquiryListDto toDto(Inquiry inquiry) {
    return InquiryListDto.builder()
        .userId(inquiry.getUser().getUserId())
        .userRole(inquiry.getUser().getRole())
        .title(inquiry.getTitle())
        .state(inquiry.isReplyState())
        .createdAt(inquiry.getCreatedAt())
        .build();
  }
}