package com.example.Ecommerce.inquiry.dto;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.user.domain.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private Long inquiryId;
  private String userId;
  private UserRole userRole;
  private String title;
  private boolean state;
  private LocalDateTime createdAt;

  public static InquiryListDto toDto(Inquiry inquiry) {
    return InquiryListDto.builder()
        .inquiryId(inquiry.getId())
        .userId(inquiry.getUser().getUserId())
        .userRole(inquiry.getUser().getRole())
        .title(inquiry.getTitle())
        .state(inquiry.isReplyState())
        .createdAt(inquiry.getCreatedAt())
        .build();
  }
}