package com.example.Ecommerce.community.service;

import com.example.Ecommerce.community.dto.post.NewPostDto;
import com.example.Ecommerce.community.dto.post.PostDetailDto;
import com.example.Ecommerce.community.dto.post.UpdatePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
  NewPostDto.Response createPost(String userId, NewPostDto.Request request);
  PostDetailDto updatePost(String userId, Long postId, UpdatePostDto updatePostDto);
  void deletePost(String userId, Long postId);
  PostDetailDto getPostById(Long postId);
  Page<PostDetailDto> getAllPosts(Pageable pageable);
  void likePost(Long postId);
}
