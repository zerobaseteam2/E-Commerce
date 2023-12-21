package com.example.Ecommerce.product.service.impl;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.InvalidOrderStatusException;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.product.domain.form.ReplyForm;
import com.example.Ecommerce.product.domain.form.ReviewForm;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.product.repository.ReviewRepository;
import com.example.Ecommerce.product.service.ReviewService;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.internal.util.collections.ReadOnlyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.ReadOnlyBufferException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository ) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public ReviewDto createReview(ReviewForm form,  User user, Order order) {
        // 주문 상태 확인
        if (order.getOrderStatus() != OrderStatus.PURCHASE_CONFIRMED) {
            throw new InvalidOrderStatusException("주문이 구매 확정 상태가 아닙니다.");
        }
        Review review = new Review();
        review.setTitle(form.getTitle());
        review.setContent(form.getContent());
        review.setStars(form.getStars());
        review.setUser(user);          // 회원 정보 설정
        review.setOrder(order); // OrderProduct 설정
        review = reviewRepository.save(review);

        return ReviewDto.from(review);
    }

    @Override
    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewForm form) {
        // Retrieve the existing review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        // Update the review's fields
        review.setTitle(form.getTitle());
        review.setContent(form.getContent());
        review.setStars(form.getStars());

        // Save the updated review
        review = reviewRepository.save(review);

        // Convert to DTO and return
        return ReviewDto.from(review);
    }


    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }


    @Override
    @Transactional
    public ReviewDto addReplyToReview(Long reviewId, String replyContent) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setReply(replyContent);
        review.setReplyState(true);

        Review updatedReview = reviewRepository.save(review);

        return ReviewDto.from(updatedReview);
    }


    @Override
    @Transactional
    public Page<ReviewDto> getReviewsByProductId(Long productId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByProductId(productId, pageable);
        return reviewPage.map(ReviewDto::from);
    }

    @Override
    @Transactional
    public Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable).map(ReviewDto::from);
    }

}




