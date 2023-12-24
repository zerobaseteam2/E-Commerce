package com.example.Ecommerce.review.repository;

public interface ReviewCustomRepository {

  Double findByStarsByProductId(Long productId);

  Long findByProductId(Long productId);
}
