package com.example.Ecommerce.community.controller;

import com.example.Ecommerce.community.dto.comment.CommentDetailDto;
import com.example.Ecommerce.community.dto.comment.NewCommentDto;
import com.example.Ecommerce.community.dto.comment.UpdateCommentDto;
import com.example.Ecommerce.community.service.CommentService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comment")
public class CommentController {

  private final CommentService commentService;


  // 댓글 작성 API
  @PostMapping("/{postId}")
  public ResponseEntity<CommentDetailDto> createComment(
      @PathVariable Long postId,
      @RequestBody @Valid NewCommentDto newCommentDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String userId = userDetails.getUser().getUserId();

    CommentDetailDto commentDetailDto = commentService.createComment(userId, postId, newCommentDto);
    return ResponseEntity.ok(commentDetailDto);
  }

  // 댓글 수정 API
  @PatchMapping("/{commentId}")
  public ResponseEntity<CommentDetailDto> updateComment(
      @PathVariable Long commentId,
      @RequestBody @Valid UpdateCommentDto updateCommentDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails){

    // 로그인한 회원 정보
    String userId = userDetails.getUser().getUserId();

    CommentDetailDto commentDetailDto = commentService.updateComment(userId, commentId, updateCommentDto);
    return ResponseEntity.ok(commentDetailDto);
  }

  // 댓글 삭제 API
  @DeleteMapping("/{commentId}")
  public ResponseEntity<?> deleteComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails){

    // 로그인한 회원 정보
    String userId = userDetails.getUser().getUserId();

    commentService.deleteComment(userId, commentId);
    return ResponseEntity.ok("댓글 삭제 성공");
  }



}
