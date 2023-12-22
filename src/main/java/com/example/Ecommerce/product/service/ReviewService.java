package com.example.Ecommerce.product.service;

import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.product.domain.form.ReviewForm;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
   ReviewDto createReview(ReviewForm form,  User user, Order order);

    ReviewDto updateReview(Long reviewId, ReviewForm form);

    void deleteReview(Long reviewId);

    ReviewDto addReplyToReview(Long reviewId, String replyContent);

    Page<ReviewDto> getReviewsByProductId(Long productId, Pageable pageable);

    Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable);
}