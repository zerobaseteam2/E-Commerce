package com.example.Ecommerce.community.service.impl;

import com.example.Ecommerce.community.domain.Comment;
import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.comment.CommentDetailDto;
import com.example.Ecommerce.community.dto.comment.NewCommentDto;
import com.example.Ecommerce.community.dto.comment.UpdateCommentDto;
import com.example.Ecommerce.community.respository.CommentRepository;
import com.example.Ecommerce.community.respository.PostRepository;
import com.example.Ecommerce.community.service.CommentService;
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

  @Override
  @Transactional
  public CommentDetailDto updateComment(String userId, Long commentId,
      UpdateCommentDto updateCommentDto) {

    // 수정하려는 댓글 가져오기
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    // 권한 확인 - 수정하려는 게시글 정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!comment.getUser().getUserId().equals(userId)) {
      throw new UnauthorizedUserException("수정하려는 댓글에 접근할 권한이 없습니다.");
    }
    // 수정
    comment.update(updateCommentDto);
    return CommentDetailDto.of(comment);
  }

  @Override
  public void deleteComment(String userId, Long commentId) {
    // 삭제하려는 댓글 가져오기
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    // 권한 확인 - 삭제하려는 게시글 정보의 회원정보와 로그인한 회원이 같은지 확인
    if (!comment.getUser().getUserId().equals(userId)) {
      throw new UnauthorizedUserException("삭제하려는 게시글에 접근할 권한이 없습니다.");
    }
    // 삭제
    commentRepository.delete(comment);
  }
}
