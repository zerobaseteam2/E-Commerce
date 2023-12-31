package com.example.Ecommerce.community.service;

import com.example.Ecommerce.community.dto.comment.CommentDetailDto;
import com.example.Ecommerce.community.dto.comment.NewCommentDto;
import com.example.Ecommerce.community.dto.comment.UpdateCommentDto;

public interface CommentService {
  CommentDetailDto createComment(String userId, Long postId, NewCommentDto newCommentDto);
  CommentDetailDto updateComment(String userId, Long commentId, UpdateCommentDto updateCommentDto);
  void deleteComment(String userId, Long commentId);
}
