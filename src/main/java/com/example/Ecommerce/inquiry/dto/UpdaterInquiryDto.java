package com.example.Ecommerce.inquiry.dto;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.inquiry.dto.RegisterInquiryDto.Response;
import com.example.Ecommerce.user.domain.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdaterInquiryDto {
  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String title;
    private String content;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private Long inquiryId;
    private String userId;
    private UserRole userRole;
    private String title;
    private String content;
    private boolean state;
    private InquiryReply inquiryReply;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Response toDto(Inquiry inquiry) {
      return Response.builder()
          .inquiryId(inquiry.getId())
          .userId(inquiry.getUser().getUserId())
          .userRole(inquiry.getUser().getRole())
          .title(inquiry.getTitle())
          .content(inquiry.getContent())
          .state(inquiry.isReplyState())
          .inquiryReply(inquiry.getInquiryReply())
          .createdAt(inquiry.getCreatedAt())
          .updatedAt(inquiry.getUpdatedAt())
          .build();
    }
  }



}
