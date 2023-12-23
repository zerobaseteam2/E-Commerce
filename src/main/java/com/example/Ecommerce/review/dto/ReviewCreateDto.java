package com.example.Ecommerce.review.dto;

import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.review.domain.Review;
import com.example.Ecommerce.user.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


public class ReviewCreateDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long productId; // 상품 id
        private Long orderProductId; // 상품주문번호
        @NotBlank(message = "제목을 입력해주세요")
        private String title;
        @NotBlank(message = "내용을 입력해주세요")
        private String content;
        @Min(value = 0, message = "별점은 0점 이상이어야 합니다")
        @Max(value = 5, message = "별점은 5점을 초과할 수 없습니다")
        private Double stars;
        
        public static Review toEntity(Request request, User user, Product product, OrderProduct orderProduct) {
            return Review.builder()
                    .user(user)
                    .product(product)
                    .orderProduct(orderProduct)
                    .username(user.getName())
                    .title(request.title)
                    .content(request.content)
                    .stars(request.stars)
                    .replyState(false)
                    .build();
        }
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
        
        public static ReviewCreateDto.Response toDto(Review review) {
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