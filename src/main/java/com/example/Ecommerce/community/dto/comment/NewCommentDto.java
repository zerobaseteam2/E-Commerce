package com.example.Ecommerce.community.dto.comment;

import com.example.Ecommerce.community.domain.Comment;
import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.community.dto.post.PostDetailDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class NewCommentDto {

  @NotBlank(message = "내용은 필수 항목입니다.")
  @Size(min = 5, message = "내용은 최소 5글자 입력해 주세요.")
  private String content;

}
