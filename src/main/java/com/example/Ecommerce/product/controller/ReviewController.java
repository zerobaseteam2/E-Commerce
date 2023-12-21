package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.repository.OrderRepository;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.product.domain.form.ReplyForm;
import com.example.Ecommerce.product.domain.form.ReviewForm;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.product.repository.ReviewRepository;
import com.example.Ecommerce.product.service.ProductService;
import com.example.Ecommerce.product.service.ReviewService;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewController(UserRepository userRepository, OrderRepository orderRepository, ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.reviewService = reviewService;
    }

    //리뷰 등록 요청
    @PostMapping("/create-review")
    public ResponseEntity<ReviewDto> registerReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ReviewForm form,
            @RequestParam("productId") OrderProduct productId) {
        // Retrieve User object based on userId
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Retrieve Order object. This is just a placeholder logic.
        // You need to implement the logic to get the order from the productId or some other way.
        Order order = orderRepository.findOrderByProductId(productId.getId())
                .orElseThrow(() -> new RuntimeException("Order not found for the given product"));
        // Create review
        ReviewDto reviewDto = reviewService.createReview(form, user, order);
        return ResponseEntity.ok(reviewDto);
    }

    // 리뷰 업데이트 요청
    @PutMapping("/reviewUpdate/{reviewId}")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewForm form) {

        // Fetch the review by reviewId
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다.")); // "Review not found"
        // Check if the logged-in user is the author of the review
        Long userId = userDetails.getUser().getId();
        if (!existingReview.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 리뷰를 수정할 권한이 없습니다."); // "You don't have permission to edit this review"
        }
        // Proceed with the update
        ReviewDto updatedReview = reviewService.updateReview(reviewId, form);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제 요청
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId) {
        // Proceed with the deletion
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/review/{reviewId}/reply")
    public ResponseEntity<?> postReplyToReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReplyForm replyForm) {

        // Check if the logged-in user is a seller
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.SELLER.getAuthority()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("이 작업을 수행할 권한이 없습니다."); // "You don't have permission to perform this action"
        }

        try {
            ReviewDto reviewDto = reviewService.addReplyToReview(reviewId, replyForm.getContent());
            return ResponseEntity.ok(reviewDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("리뷰를 찾을 수 없습니다."); // "Review not found"
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("이미 답변이 등록된 리뷰입니다."); // "This review already has a reply"
        }
    }


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

        Long currentUserId = userDetails.getUser().getId(); // 사용자 ID를 가져옵니다.
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDto> reviewPage = reviewService.getReviewsByUserId(currentUserId, pageable);
        return ResponseEntity.ok(reviewPage);
    }


}