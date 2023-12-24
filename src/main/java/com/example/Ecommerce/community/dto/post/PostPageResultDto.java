package com.example.Ecommerce.community.dto.post;

import com.example.Ecommerce.community.dto.PageInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPageResultDto {

  private PageInfoDto pageInfoDto;
  private List<PostDetailDto> posts;

  public static PostPageResultDto of(Page<PostDetailDto> result) {
    PageInfoDto pageInfoDto = PageInfoDto.builder()
        .page(result.getNumber())
        .size(result.getSize())
        .totalPosts(result.getTotalElements())
        .totalPage(result.getTotalPages())
        .build();

    return PostPageResultDto.builder()
        .pageInfoDto(pageInfoDto)
        .posts(result.getContent())
        .build();
  }
}

