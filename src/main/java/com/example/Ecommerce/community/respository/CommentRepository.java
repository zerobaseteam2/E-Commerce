package com.example.Ecommerce.community.respository;

import com.example.Ecommerce.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
