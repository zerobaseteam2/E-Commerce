package com.example.Ecommerce.community.domain;

import com.example.Ecommerce.community.dto.comment.NewCommentDto;
import com.example.Ecommerce.community.dto.comment.UpdateCommentDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
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
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  @CreatedDate
  private Date createdAt; //생성날짜

  @LastModifiedDate
  private Date updatedAt; //수정날짜

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  public static Comment create(NewCommentDto newCommentDto, User user, Post post) {
    return Comment.builder()
        .content(newCommentDto.getContent())
        .user(user)
        .post(post)
        .build();
  }

  public void update(UpdateCommentDto updateCommentDto) {
    this.content = updateCommentDto.getContent();
  }
}
