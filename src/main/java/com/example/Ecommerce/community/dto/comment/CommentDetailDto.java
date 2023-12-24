package com.example.Ecommerce.community.dto.comment;

import com.example.Ecommerce.community.domain.Comment;
import com.example.Ecommerce.community.domain.Post;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetailDto {

  private Long commentId;
  private String content;
  private Long postId;
  private String userId;
  private Date createdAt; //생성날짜
  private Date updatedAt; //수정날짜


  // Comment entity -> CommentDetailDto 변경하여 반환
  public static CommentDetailDto of(Comment comment) {
    return CommentDetailDto.builder()
        .commentId(comment.getId())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .postId(comment.getPost().getId())
        .userId(comment.getUser().getUserId())
        .build();
  }
}
