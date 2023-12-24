package com.example.Ecommerce.community.service;

import com.example.Ecommerce.community.dto.NewPostDto;
import com.example.Ecommerce.community.dto.PostDetailDto;
import com.example.Ecommerce.community.dto.UpdatePostDto;

public interface PostService {
  PostDetailDto createPost(String customerId, NewPostDto newPostDto);
  PostDetailDto updatePost(String customerId, Long postId, UpdatePostDto updatePostDto);
  void deletePost(String customerId, Long postId);
  PostDetailDto getPostById(Long postId);
}
