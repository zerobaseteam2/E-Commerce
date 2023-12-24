package com.example.Ecommerce.community.respository;

import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.user.domain.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Transactional
  @Modifying
  @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
  void increaseViewCount(@Param("postId") Long postId);
}
