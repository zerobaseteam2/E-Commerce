package com.example.Ecommerce.review.dto;

import com.example.Ecommerce.review.domain.Review;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ReplyDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

  }

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

    public static Response toDto(Review review) {
      return Response.builder()
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