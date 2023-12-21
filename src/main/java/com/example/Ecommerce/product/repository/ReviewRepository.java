package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Review;
import com.example.Ecommerce.product.dto.ReviewDto;
import com.example.Ecommerce.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // List<Review> findByProductId(Long productId);
   // Review findById(Long Id);
    //List<Review> findAllByUserId(User userId);
    Page<Review> findByProductId(Long productId, Pageable pageable);

    Page<Review> findByUserId(Long userId,  Pageable pageable);
}