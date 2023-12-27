package com.example.Ecommerce.inquiry.dto.admin;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RegisterInquiryReplyDto {
  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotNull
    private Long inquiryId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static InquiryReply toEntity(RegisterInquiryReplyDto.Request request, Inquiry inquiry, User user) {
      return InquiryReply.builder()
          .inquiry(inquiry)
          .admin(user)
          .title(request.getTitle())
          .content(request.getContent())
          .build();
    }
  }
}
