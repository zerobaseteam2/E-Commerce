package com.example.Ecommerce.community.dto;

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
public class PageInfoDto {

  private int page;
  private int size;
  private long totalPosts;
  private int totalPage;

}