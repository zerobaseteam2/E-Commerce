package com.example.Ecommerce.community.respository;

import com.example.Ecommerce.community.domain.Comment;
import com.example.Ecommerce.community.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost(Post post);
}
