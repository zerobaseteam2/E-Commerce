package com.example.Ecommerce.community.service.impl;

import com.example.Ecommerce.community.domain.Comment;
import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.post.NewPostDto;
import com.example.Ecommerce.community.dto.post.PostDetailDto;
import com.example.Ecommerce.community.dto.post.UpdatePostDto;
import com.example.Ecommerce.community.respository.CommentRepository;
import com.example.Ecommerce.community.respository.PostRepository;
import com.example.Ecommerce.community.service.PostService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.exception.UnauthorizedUserException;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;

  @Override
  @Transactional
  public NewPostDto.Response createPost(String userId, NewPostDto.Request request) {

    // 입력값으로 받은 userId 로 찾은 회원(구매자)
    Optional<User> user = userRepository.findByUserId(userId);
    if (user.isEmpty()) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    // 새로운 게시글
    Post post = Post.create(request, user.get());
    postRepository.save(post);
    // 생성한 게시글 상세내역 반환
    return NewPostDto.Response.of(post);
  }

  @Override
  @Transactional
  public PostDetailDto updatePost(String userId, Long postId, UpdatePostDto updatePostDto) {

    // 수정하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    // 권한 확인 - 수정하려는 게시글 정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!post.getUser().getUserId().equals(userId)) {
      throw new UnauthorizedUserException("수정하려는 게시글에 접근할 권한이 없습니다.");
    }
    // 수정
    post.update(updatePostDto);
    return PostDetailDto.of(post);
  }

  @Override
  public void deletePost(String userId, Long postId) {
    // 삭제하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    // 권한 확인 - 삭제하려는 게시글 정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!post.getUser().getUserId().equals(userId)) {
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
    // 댓글 찾기
    List<Comment> comments = commentRepository.findByPost(post);
    return PostDetailDto.of(post);
  }

  @Override
  public Page<PostDetailDto> getAllPosts(Pageable pageable) {
    Page<Post> postsPage = postRepository.findAll(pageable);
    return postsPage.map(PostDetailDto::of);
  }

  @Override
  public void likePost(Long postId) {

    // 좋아요하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
    // 좋아요 증가
    postRepository.increaseLikeCount(postId);
  }

}
