package com.example.Ecommerce.community.respository;

import com.example.Ecommerce.community.domain.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Transactional
  @Modifying
  @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
  void increaseViewCount(@Param("postId") Long postId);
  Page<Post> findAll(Pageable pageable);

  @Transactional
  @Modifying
  @Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :postId")
  void increaseLikeCount(@Param("postId") Long postId);
}
