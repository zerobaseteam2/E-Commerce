package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.form.ReviewForm;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.product.service.ProductService;
import com.example.Ecommerce.product.service.ReviewService;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

     //리뷰 등록 요청
    @PostMapping("/register")
    public ResponseEntity<ReviewDto> registerReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ReviewForm form,
            @RequestParam("productId") Product productId) {
        User userId = userDetails.getUser().getId();
   //     Product product = productService.getProductById(productId); // 상품 조회 로직 추가 해야함.
        ReviewDto reviewDto = reviewService.createReview(form, userId, productId);
        return ResponseEntity.ok(reviewDto);
    }

    // 리뷰 업데이트 요청
    @PutMapping("/reviewUpdate")
    public ResponseEntity<ReviewDto> updateReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewForm form) {
        User userId = userDetails.getUser().getId();
        ReviewDto updatedReview = reviewService.updateReview(reviewId, form);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제 요청
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    // 특정 리뷰 조회 요청
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long reviewId) {
        ReviewDto reviewDto = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(reviewDto);
    }

//    // 특정 사용자의 모든 리뷰 조회 요청
//    @GetMapping("/user")
//    public ResponseEntity<List<ReviewDto>> getAllReviewsByUser(
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Long userId = userDetails.getUser().getId();
//        List<ReviewDto> reviews = reviewService.getAllReviewsByCustomerId(userId);
//        return ResponseEntity.ok(reviews);
//    }

    @GetMapping("/products/{productId}/reviews")
    public Page<ReviewDto> listReviews(@PathVariable Long productId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getReviewsByProductId(productId, pageable);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDto>> listMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UN_AUTHORIZED);
        }
        Long currentUserId = userDetails.getUserId(); // UserDetailsImpl에서 제공하는 메서드를 사용하여 사용자 ID를 가져옵니다.
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDto> reviewPage = reviewService.getReviewsByUserId(currentUserId, pageable);
        return ResponseEntity.ok(reviewPage);
    }



}