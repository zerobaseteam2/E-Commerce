package com.example.Ecommerce.product.service;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.product.domain.form.ReviewForm;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
   ReviewDto createReview(ReviewForm form, User userId, Product productId); //Order orderDetailId

    ReviewDto updateReview(Long reviewId, ReviewForm form);

    void deleteReview(Long reviewId);

    ReviewDto getReviewById(Long reviewId);

    void addReplyToReview(Long reviewId, String reply, String username);

    Page<ReviewDto> getReviewsByProductId(Long productId, Pageable pageable);

 //   List<ReviewDto> getAllReviewsByCustomerId(User userId, Pageable pageable);
   Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable);
}