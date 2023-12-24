package com.example.Ecommerce.community.service.impl;

import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.NewPostDto;
import com.example.Ecommerce.community.dto.PostDetailDto;
import com.example.Ecommerce.community.respository.PostRepository;
import com.example.Ecommerce.community.service.PostService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public PostDetailDto createPost(String customerId, NewPostDto newPostDto) {

    // 입력값으로 받은 customerId 로 찾은 회원(구매자)
    Optional<User> user = userRepository.findByUserId(customerId);
    if (user.isEmpty()) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    // 새로운 게시글
    Post post = Post.create(newPostDto, user.get());
    postRepository.save(post);
    // 생성한 게시글 상세내역 반환
    return PostDetailDto.of(post);
  }


}
