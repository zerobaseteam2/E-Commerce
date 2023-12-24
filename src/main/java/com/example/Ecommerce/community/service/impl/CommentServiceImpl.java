package com.example.Ecommerce.community.service.impl;

import com.example.Ecommerce.community.domain.Comment;
import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.comment.CommentDetailDto;
import com.example.Ecommerce.community.dto.comment.NewCommentDto;
import com.example.Ecommerce.community.respository.CommentRepository;
import com.example.Ecommerce.community.respository.PostRepository;
import com.example.Ecommerce.community.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Override
  @Transactional
  public CommentDetailDto createComment(String userId, Long postId,
      NewCommentDto newCommentDto) {

    // 입력값으로 받은 userId 로 찾은 회원(구매자)
    Optional<User> user = userRepository.findByUserId(userId);
    if (user.isEmpty()) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
    // 댓글 작성하려는 게시글 가져오기
    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

    // 댓글 생성
    Comment comment = Comment.create(newCommentDto, user.get(), post);
    commentRepository.save(comment);
    return CommentDetailDto.of(comment);
  }
}
