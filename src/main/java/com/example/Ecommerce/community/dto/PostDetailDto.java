package com.example.Ecommerce.community.dto;

import com.example.Ecommerce.community.domain.Post;
import java.time.LocalDate;
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

  private Long id;
  private String customerId; //작성 회원 아이디
  private String title;
  private String content;
  private Long viewCount;
  private Long likeCount;
  private LocalDate createdAt; //생성날짜
  private LocalDate updatedAt; //수정날짜


  // Post entity -> PostDetailDto 변경하여 반환
  public static PostDetailDto of(Post post) {
    return PostDetailDto.builder()
        .id(post.getId())
        .customerId(post.getUser().getUserId())
        .title(post.getTitle())
        .content(post.getContent())
        .viewCount(post.getViewCount())
        .likeCount(post.getLikeCount())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
