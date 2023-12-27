package com.example.Ecommerce.inquiry.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdateInquiryReplyDto {
  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotNull
    private Long inquiryReplyId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

  }
}
