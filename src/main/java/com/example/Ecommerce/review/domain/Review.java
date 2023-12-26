package com.example.Ecommerce.review.domain;


import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.review.dto.ReviewUpdateDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "review")
@DynamicUpdate
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orderProduct_id", nullable = false)
  private OrderProduct orderProduct;

  @Column(nullable = false)
  private String username;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "stars")
  private Double stars;

  @Column(name = "replyState")
  private Boolean replyState;

  @Column(name = "reply")
  private String reply;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

  public void update(ReviewUpdateDto.Request request) {
    this.title = request.getTitle();
    this.content = request.getContent();
    this.stars = request.getStars();
  }

  public void addReply(String reply) {
    this.replyState = true;
    this.reply = reply;
  }

}