package com.example.Ecommerce.community.domain;

import com.example.Ecommerce.community.dto.NewPostDto;
import com.example.Ecommerce.community.dto.UpdatePostDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  private Long viewCount;

  private Long likeCount;

  @Column(nullable = false)
  @CreatedDate
  private LocalDate createdAt; //생성날짜

  @LastModifiedDate
  private LocalDate updatedAt; //수정날짜

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;


  public static Post create(NewPostDto newPostDto, User user) {
    return Post.builder()
        .title(newPostDto.getTitle())
        .content(newPostDto.getContent())
        .user(user)
        .viewCount(0L) //초기값 0
        .likeCount(0L) //초기값 0
        .build();
  }

  public void update(UpdatePostDto updatePostDto) {
    if (updatePostDto.getTitle() != null) {
      this.title = updatePostDto.getTitle();
    }
    if (updatePostDto.getContent() != null){
      this.content = updatePostDto.getContent();
    }
  }
}
