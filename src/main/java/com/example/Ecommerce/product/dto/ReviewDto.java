package com.example.Ecommerce.product.dto;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.user.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private User userId;
    private Product productId;
   // private Long orderDetailId; 주문 번호
    private String name;
    private String title;
    private String content;
    private Double stars;
    private Boolean replyState;
    private String reply;

    // Constructor, Getters, and Setters

    public static ReviewDto from(Review review) {
        ReviewDto dto = new ReviewDto();

        dto.setUserId(review.getUserId());
        dto.setProductId(review.getProductId());
       // dto.setOrderDetailId(review.getOrderDetail().getId());  주문 번호
        dto.setTitle(review.getTitle());
        dto.setContent(review.getContent());
        dto.setStars(review.getStars());
        dto.setReplyState(review.getReplyState());
        dto.setReply(review.getReply());
        return dto;
    }
}