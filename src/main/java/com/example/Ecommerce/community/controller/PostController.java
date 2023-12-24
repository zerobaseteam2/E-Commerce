package com.example.Ecommerce.community.controller;

import com.example.Ecommerce.community.dto.NewPostDto;
import com.example.Ecommerce.community.dto.PostDetailDto;
import com.example.Ecommerce.community.dto.UpdatePostDto;
import com.example.Ecommerce.community.service.PostService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/post")
public class PostController {

  private final PostService postService;

  // 게시글 등록 API
  @PostMapping
  public ResponseEntity<PostDetailDto> post(
      @RequestBody @Valid NewPostDto newPostDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();
    PostDetailDto postDetailDto = postService.createPost(customerId, newPostDto);
    return ResponseEntity.ok(postDetailDto);
  }

  // 게시글 수정 API
  @PutMapping("{postId}")
  public ResponseEntity<PostDetailDto> updatePost(
      @PathVariable Long postId,
      @RequestBody @Valid UpdatePostDto updatePostDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();
    PostDetailDto postDetailDto = postService.updatePost(customerId, postId, updatePostDto);
    return ResponseEntity.ok(postDetailDto);
  }

  // 게시글 삭제 API
  @DeleteMapping("/{postId}")
  public ResponseEntity<?> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String customerId = userDetails.getUser().getUserId();
    postService.deletePost(customerId, postId);
    return ResponseEntity.ok("게시글 삭제 성공");
  }

}
