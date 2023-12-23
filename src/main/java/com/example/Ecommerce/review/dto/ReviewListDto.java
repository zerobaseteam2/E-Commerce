package com.example.Ecommerce.review.dto;

import com.example.Ecommerce.review.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

public class ReviewListDto {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private Long customerId; // 고객 id
    private String customerName;
    private Long productId;  // 상품 번호
    private Long orderProductId; // 상품주문번호
    private String title;
    private String content;
    private Double stars;
    private Boolean replyState;
    private String reply;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    
    public static ReviewListDto.Response toDto(Review review) {
      return ReviewListDto.Response.builder()
              .customerId(review.getUser().getId())
              .customerName(review.getUsername())
              .productId(review.getProduct().getId())
              .orderProductId(review.getOrderProduct().getId())
              .title(review.getTitle())
              .content(review.getContent())
              .stars(review.getStars())
              .replyState(review.getReplyState())
              .reply(review.getReply())
              .createAt(review.getCreateAt())
              .modifiedAt(review.getModifiedAt())
              .build();
    }
  }
  
}
