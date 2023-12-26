package com.example.Ecommerce.inquiry.dto;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RegisterInquiryDto {
  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String title;
    private String content;

    public static Inquiry toEntity(RegisterInquiryDto.Request request, User user) {
      return Inquiry.builder()
          .user(user)
          .title(request.title)
          .content(request.content)
          .replyState(false)
          .build();
    }
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String userId;
    private UserRole userRole;
    private String title;
    private String content;
    private boolean state;
    private InquiryReply inquiryReply;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public static Response toDto(Inquiry inquiry) {
      return Response.builder()
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
