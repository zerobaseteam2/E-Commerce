package com.example.Ecommerce.review.service;

import com.example.Ecommerce.review.dto.ReplyDto;
import com.example.Ecommerce.review.dto.ReviewCreateDto;
import com.example.Ecommerce.review.dto.ReviewListDto;
import com.example.Ecommerce.review.dto.ReviewUpdateDto;
import com.example.Ecommerce.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  ReviewCreateDto.Response createReview(ReviewCreateDto.Request request, User user);

  ReviewUpdateDto.Response updateReview(Long reviewId, ReviewUpdateDto.Request request);

  void deleteReview(Long reviewId);

  ReplyDto.Response addReplyToReview(Long reviewId, ReplyDto.Request request, User user);

  Page<ReviewListDto.Response> getReviewsByProductId(Long productId, Pageable pageable);

  Page<ReviewListDto.Response> getReviewsByUserId(Long userId, Pageable pageable);
}