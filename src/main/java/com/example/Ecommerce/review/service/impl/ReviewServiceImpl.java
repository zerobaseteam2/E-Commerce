package com.example.Ecommerce.review.service.impl;

import static com.example.Ecommerce.exception.ErrorCode.NOT_PURCHASE_CONFIRMED;
import static com.example.Ecommerce.exception.ErrorCode.NOT_SELLER_OF_THE_PRODUCT;
import static com.example.Ecommerce.exception.ErrorCode.ORDER_PRODUCT_NOT_FOUND;
import static com.example.Ecommerce.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.example.Ecommerce.exception.ErrorCode.REPLY_EXISTS;
import static com.example.Ecommerce.exception.ErrorCode.REVIEW_NOT_FOUND;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.repository.OrderProductRepository;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.repository.ProductRepository;
import com.example.Ecommerce.review.domain.Review;
import com.example.Ecommerce.review.dto.ReplyDto;
import com.example.Ecommerce.review.dto.ReviewCreateDto;
import com.example.Ecommerce.review.dto.ReviewListDto;
import com.example.Ecommerce.review.dto.ReviewUpdateDto;
import com.example.Ecommerce.review.repository.ReviewCustomRepository;
import com.example.Ecommerce.review.repository.ReviewRepository;
import com.example.Ecommerce.review.service.ReviewService;
import com.example.Ecommerce.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final OrderProductRepository orderProductRepository;
  private final ProductRepository productRepository;
  private final ReviewCustomRepository reviewCustomRepository;

  @Override
  @Transactional
  public ReviewCreateDto.Response createReview(ReviewCreateDto.Request request, User user) {
    // productId 확인
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

    // orderProductId 확인
    OrderProduct orderProduct = orderProductRepository.findById(request.getOrderProductId())
        .orElseThrow(() -> new CustomException(ORDER_PRODUCT_NOT_FOUND));

    // 주문 상태 확인
    if (orderProduct.getStatus() != OrderStatus.PURCHASE_CONFIRMED) {
      throw new CustomException(NOT_PURCHASE_CONFIRMED);
    }

    Review review = reviewRepository.save(
        ReviewCreateDto.Request.toEntity(request, user, product, orderProduct)
    );

    // 리뷰 등록시 상품 별점 업데이트
    Long totalCount = reviewCustomRepository.findByProductId(product.getId());
    Double sum = reviewCustomRepository.findByStarsByProductId(product.getId());

    double stars = sum / totalCount;
    product.setStars(stars);

    return ReviewCreateDto.Response.toDto(review);
  }

  @Override
  @Transactional
  public ReviewUpdateDto.Response updateReview(Long reviewId, ReviewUpdateDto.Request request) {

    // 리뷰가 존재하는 지 확인
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

    // Update the review's fields
    review.update(request);

    // 리뷰 수정시 상품 별점 업데이트
    Product product = productRepository.findById(review.getProduct().getId())
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

    Long totalCount = reviewCustomRepository.findByProductId(product.getId());
    Double sum = reviewCustomRepository.findByStarsByProductId(product.getId());

    double stars = sum / totalCount;
    product.setStars(stars);

    // Convert to DTO and return
    return ReviewUpdateDto.Response.toDto(review);
  }


  @Override
  @Transactional
  public void deleteReview(Long reviewId) {

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

    Product product = productRepository.findById(review.getProduct().getId())
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
    reviewRepository.delete(review);

    // 별점도 수정
    Long totalCount = reviewCustomRepository.findByProductId(product.getId());
    Double sum = reviewCustomRepository.findByStarsByProductId(product.getId());
    if (sum != null) {
      double stars = sum / totalCount;
      product.setStars(stars);
    } else {
      product.setStars(0.0);
    }
  }


  @Override
  @Transactional
  public ReplyDto.Response addReplyToReview(Long reviewId, ReplyDto.Request request, User user) {
    // 리뷰가 존재하는 지 확인
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

    // 답변 등록 여부 확인
    if (review.getReplyState()) {
      throw new CustomException(REPLY_EXISTS);
    }

    // 요청을 한 판매자가 해당 상품의 판매자가 맞는지 확인
    Product nowProduct = productRepository.findById(review.getProduct().getId())
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
    if (!nowProduct.getSellerId().equals(user.getId())) {
      throw new CustomException(NOT_SELLER_OF_THE_PRODUCT);
    }

    review.addReply(request.getContent());

    return ReplyDto.Response.toDto(review);
  }


  @Override
  @Transactional
  public Page<ReviewListDto.Response> getReviewsByProductId(Long productId, Pageable pageable) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    Page<Review> reviewPage = reviewRepository.findByProduct(product, pageable);
    return reviewPage.map(ReviewListDto.Response::toDto);
  }

  @Override
  @Transactional
  public Page<ReviewListDto.Response> getReviewsByUserId(Long userId, Pageable pageable) {
    return reviewRepository.findByUserId(userId, pageable).map(ReviewListDto.Response::toDto);
  }
}




