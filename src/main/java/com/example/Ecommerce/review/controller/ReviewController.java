package com.example.Ecommerce.review.controller;

import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.order.repository.OrderRepository;
import com.example.Ecommerce.review.dto.ReplyDto;
import com.example.Ecommerce.review.dto.ReviewCreateDto;
import com.example.Ecommerce.review.dto.ReviewListDto;
import com.example.Ecommerce.review.dto.ReviewUpdateDto;
import com.example.Ecommerce.review.repository.ReviewRepository;
import com.example.Ecommerce.review.service.ReviewService;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewService reviewService;
    private final OrderProductRepository orderProductRepository;
    private final ReviewRepository reviewRepository;


    //리뷰 등록 요청
    @PostMapping("/create-review")
    public ResponseEntity<ReviewCreateDto.Response> registerReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ReviewCreateDto.Request request) {
        // Retrieve User object based on userId
        User user = userDetails.getUser();
        
        // Create review
        ReviewCreateDto.Response response = reviewService.createReview(request, user);
        return ResponseEntity.ok(response);
    }

    // 리뷰 업데이트 요청
    @PutMapping("/reviewUpdate/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateDto.Request request) {
        
        // Proceed with the update
        ReviewUpdateDto.Response response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제 요청
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId) {
        // Proceed with the deletion
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/review/{reviewId}/reply")
    public ResponseEntity<ReplyDto.Response> postReplyToReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReplyDto.Request request) {
        User user = userDetails.getUser();
        ReplyDto.Response response = reviewService.addReplyToReview(reviewId, request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{productId}/reviews")
    public Page<ReviewListDto.Response> listReviews(@PathVariable Long productId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getReviewsByProductId(productId, pageable);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewListDto.Response>> listMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {

        Long currentUserId = userDetails.getUser().getId(); // 사용자 ID를 가져옵니다.
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewListDto.Response> reviewPage = reviewService.getReviewsByUserId(currentUserId, pageable);
        return ResponseEntity.ok(reviewPage);
    }
}