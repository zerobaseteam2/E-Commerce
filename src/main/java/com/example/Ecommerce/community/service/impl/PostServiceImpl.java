package com.example.Ecommerce.community.service.impl;

import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.NewPostDto;
import com.example.Ecommerce.community.dto.PostDetailDto;
import com.example.Ecommerce.community.dto.UpdatePostDto;
import com.example.Ecommerce.community.respository.PostRepository;
import com.example.Ecommerce.community.service.PostService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.UnauthorizedUserException;
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

  @Override
  @Transactional
  public PostDetailDto updatePost(String customerId, Long postId, UpdatePostDto updatePostDto) {

    // 수정하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    // 권한 확인 - 수정하려는 게시글 정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!post.getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("수정하려는 게시글에 접근할 권한이 없습니다.");
    }
    // 수정
    post.update(updatePostDto);
    return PostDetailDto.of(post);
  }

  @Override
  public void deletePost(String customerId, Long postId) {
    // 삭제하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    // 권한 확인 - 삭제하려는 게시글 정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!post.getUser().getUserId().equals(customerId)) {
      throw new UnauthorizedUserException("삭제하려는 게시글에 접근할 권한이 없습니다.");
    }
    // 삭제
    postRepository.delete(post);
  }

  @Override
  public PostDetailDto getPostById(Long postId) {
    // 조회하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    // 조회수 변경
    postRepository.increaseViewCount(postId);
    return PostDetailDto.of(post);
  }


}
