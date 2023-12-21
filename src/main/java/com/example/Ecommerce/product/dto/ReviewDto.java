package com.example.Ecommerce.product.dto;

import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long user;          // User의 ID
    private Long orderProduct;  // OrderProduct의 ID
    private Long order;         // 주문번호
    private String name;
    private String title;
    private String content;
    private Double stars;
    private Boolean replyState;
    private String reply;

    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    // Constructor, Getters, and Setters

    public static ReviewDto from(Review review) {
        ReviewDto dto = new ReviewDto();

        dto.setUser(review.getUser().getId());          // User 객체에서 ID 추출
        dto.setOrder(review.getOrder().getId());        // Order 객체에서 ID 추출
        dto.setOrderProduct(review.getOrderProduct().getId()); // OrderProduct 객체에서 ID 추출
        dto.setTitle(review.getTitle());
        dto.setContent(review.getContent());
        dto.setStars(review.getStars());
        dto.setReplyState(review.getReplyState());
        dto.setReply(review.getReply());
        dto.setCreateAt(review.getCreateAt());
        dto.setModifiedAt(review.getModifiedAt());
        return dto;
    }
}