package com.example.Ecommerce.community.dto.post;

import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.comment.CommentDetailDto;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
public class PostDetailDto {

  private Long postId;
  private String userId; //작성 회원 아이디
  private String title;
  private String content;
  private Long viewCount;
  private Long likeCount;
  private Date createdAt; //생성날짜
  private Date updatedAt; //수정날짜
  private List<CommentDetailDto> commentList;


  // Post entity -> PostDetailDto 변경하여 반환
  public static PostDetailDto of(Post post) {

    List<CommentDetailDto> commentDetailDtoList = post.getCommentList()
        .stream()
        .map(CommentDetailDto::of)
        .collect(Collectors.toList());

    return PostDetailDto.builder()
        .postId(post.getId())
        .userId(post.getUser().getUserId())
        .title(post.getTitle())
        .content(post.getContent())
        .viewCount(post.getViewCount())
        .likeCount(post.getLikeCount())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .commentList(commentDetailDtoList)
        .build();
  }
}
