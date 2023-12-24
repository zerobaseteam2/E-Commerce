package com.example.Ecommerce.community.service;

import com.example.Ecommerce.community.dto.NewPostDto;
import com.example.Ecommerce.community.dto.PostDetailDto;

public interface PostService {
  PostDetailDto createPost(String customerId, NewPostDto newPostDto);

}
