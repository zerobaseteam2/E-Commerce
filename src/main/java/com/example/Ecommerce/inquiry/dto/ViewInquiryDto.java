package com.example.Ecommerce.inquiry.dto;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.inquiry.dto.admin.ViewInquiryReplyDto;
import com.example.Ecommerce.user.domain.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewInquiryDto {
  private Long inquiryId;
  private String userId;
  private UserRole userRole;
  private String title;
  private String content;
  private boolean state;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private ViewInquiryReplyDto inquiryReply;

  public static ViewInquiryDto toDto(Inquiry inquiry, ViewInquiryReplyDto inquiryReply) {
    return ViewInquiryDto.builder()
        .inquiryId(inquiry.getId())
        .userId(inquiry.getUser().getUserId())
        .userRole(inquiry.getUser().getRole())
        .title(inquiry.getTitle())
        .content(inquiry.getContent())
        .state(inquiry.isReplyState())
        .createdAt(inquiry.getCreatedAt())
        .updatedAt(inquiry.getUpdatedAt())
        .inquiryReply(inquiryReply)
        .build();
  }
  public static ViewInquiryDto toDto(Inquiry inquiry) {
    return ViewInquiryDto.builder()
            .inquiryId(inquiry.getId())
            .userId(inquiry.getUser().getUserId())
            .userRole(inquiry.getUser().getRole())
            .title(inquiry.getTitle())
            .content(inquiry.getContent())
            .state(inquiry.isReplyState())
            .createdAt(inquiry.getCreatedAt())
            .updatedAt(inquiry.getUpdatedAt())
            .build();
  }
}
