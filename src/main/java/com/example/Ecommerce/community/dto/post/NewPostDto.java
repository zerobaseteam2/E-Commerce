package com.example.Ecommerce.community.dto.post;

import com.example.Ecommerce.community.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class NewPostDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(min = 5, message = "제목은 최소 5글자 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(min = 5, message = "내용은 최소 5글자 입력해 주세요.")
    private String content;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long postId;
    private String userId; //작성 회원 아이디
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Date createdAt; //생성날짜
    private Date updatedAt; //수정날짜


    // Post entity -> PostDetailDto 변경하여 반환
    public static NewPostDto.Response of(Post post) {

      return NewPostDto.Response.builder()
          .postId(post.getId())
          .userId(post.getUser().getUserId())
          .title(post.getTitle())
          .content(post.getContent())
          .viewCount(post.getViewCount())
          .likeCount(post.getLikeCount())
          .createdAt(post.getCreatedAt())
          .updatedAt(post.getUpdatedAt())
          .build();
    }

    }
  }
