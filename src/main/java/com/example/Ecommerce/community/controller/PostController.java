package com.example.Ecommerce.community.controller;

import com.example.Ecommerce.community.dto.post.NewPostDto;
import com.example.Ecommerce.community.dto.post.PostDetailDto;
import com.example.Ecommerce.community.dto.post.PostPageResultDto;
import com.example.Ecommerce.community.dto.post.UpdatePostDto;
import com.example.Ecommerce.community.service.PostService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class PostController {

  private final PostService postService;

  // 게시글 등록 API
  @PostMapping("/post")
  public ResponseEntity<NewPostDto.Response> post(
      @RequestBody @Valid NewPostDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String userId = userDetails.getUser().getUserId();
    NewPostDto.Response response = postService.createPost(userId, request);
    return ResponseEntity.ok(response);
  }

  // 게시글 수정 API
  @PutMapping("/post/{postId}")
  public ResponseEntity<PostDetailDto> updatePost(
      @PathVariable Long postId,
      @RequestBody @Valid UpdatePostDto updatePostDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String userId = userDetails.getUser().getUserId();
    PostDetailDto postDetailDto = postService.updatePost(userId, postId, updatePostDto);
    return ResponseEntity.ok(postDetailDto);
  }

  // 게시글 삭제 API
  @DeleteMapping("/post/{postId}")
  public ResponseEntity<?> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    // 로그인한 회원 정보
    String userId = userDetails.getUser().getUserId();
    postService.deletePost(userId, postId);
    return ResponseEntity.ok("게시글 삭제 성공");
  }

  // 게시글 조회 API
  @GetMapping("/post/{postId}")
  public ResponseEntity<PostDetailDto> getPost(@PathVariable Long postId) {
    PostDetailDto postDetailDto = postService.getPostById(postId);
    return ResponseEntity.ok(postDetailDto);
  }

  // 모든 게시글 목록 조회 API
  @GetMapping("/posts/list")
  public ResponseEntity<?> getAllPosts(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<PostDetailDto> result = postService.getAllPosts(pageable);

    return ResponseEntity.ok(PostPageResultDto.of(result));

  }

  // 게시글 좋아요 API
  @PutMapping("/post/{postId}/like")
  public ResponseEntity<?> likePost(
      @PathVariable Long postId) {
    postService.likePost(postId);
    return ResponseEntity.ok("게시글 좋아요 성공");
  }

}
