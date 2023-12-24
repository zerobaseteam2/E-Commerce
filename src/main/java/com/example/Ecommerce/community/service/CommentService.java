package com.example.Ecommerce.community.service;

import com.example.Ecommerce.community.dto.comment.CommentDetailDto;
import com.example.Ecommerce.community.dto.comment.NewCommentDto;

public interface CommentService {
  CommentDetailDto createComment(String userId, Long postId, NewCommentDto newCommentDto);
}
