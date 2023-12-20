package com.example.Ecommerce.product.service.impl;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.product.domain.form.ReviewForm;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.product.repository.ReviewRepository;
import com.example.Ecommerce.product.service.ReviewService;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import com.example.Ecommerce.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    // 추가로 필요한 Repository 주입

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository ,UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ReviewDto createReview(ReviewForm form, User userId, Product productId) { //, OrderDetailId orderDetailId.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 로그인하지 않았을 경우 예외 발생
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }

        Review review = new Review();
        review.setTitle(form.getTitle());
        review.setContent(form.getContent());
        review.setStars(form.getStars());
        review = reviewRepository.save(review);

        return ReviewDto.from(review);
    }

    @Override
    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewForm form) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 로그인하지 않았을 경우 예외 발생
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }
        // 현재 로그인한 사용자의 ID 확인
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUserId(currentUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 리뷰 작성자와 현재 사용자가 동일한지 확인
        if (!review.getUserId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }

        review.setTitle(form.getTitle());
        review.setContent(form.getContent());
        review.setStars(form.getStars());
        review = reviewRepository.save(review);

        return ReviewDto.from(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 로그인하지 않았을 경우 예외 발생
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewDto getReviewById(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 로그인하지 않았을 경우 예외 발생
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        return ReviewDto.from(review);
    }

//    @Override
//    public List<ReviewDto> getAllReviewsByCustomerId(Long customerId) {
//        List<Review> reviews = reviewRepository.findAllByCustomerId(customerId);
//        return reviews.stream()
//                .map(ReviewDTO::from)
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public void addReplyToReview(Long reviewId, String reply, String username) {
        if (reply == null || reply.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getRole().equals(UserRole.SELLER)) {
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (review.getReply() != null && !review.getReply().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }

//        Product product = productRepository.findById(review.getProductId())
//                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
//
//        if (!product.getSellerId().equals(user.getId())) {
//            throw new CustomException(ErrorCode.UN_AUTHORIZED);
//        }
        review.setReply(reply);
        review.setReplyState(true);
        reviewRepository.save(review);
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




