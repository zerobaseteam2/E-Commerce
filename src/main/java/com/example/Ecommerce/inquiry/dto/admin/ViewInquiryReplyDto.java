package com.example.Ecommerce.inquiry.dto.admin;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.user.domain.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewInquiryReplyDto {
  private Long inquiryReplyId;
  private String adminId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static ViewInquiryReplyDto toDto(InquiryReply inquiryReply) {
    return ViewInquiryReplyDto.builder()
        .inquiryReplyId(inquiryReply.getId())
        .adminId(inquiryReply.getAdmin().getUserId())
        .title(inquiryReply.getTitle())
        .content(inquiryReply.getContent())
        .createdAt(inquiryReply.getCreatedAt())
        .updatedAt(inquiryReply.getUpdatedAt())
        .build();
  }
}
