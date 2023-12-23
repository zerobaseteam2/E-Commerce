package com.example.Ecommerce.review.repository;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  Page<Review> findByProduct(Product product, Pageable pageable);

  Page<Review> findByUserId(Long userId, Pageable pageable);
}