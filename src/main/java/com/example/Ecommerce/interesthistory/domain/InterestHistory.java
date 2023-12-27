package com.example.Ecommerce.interesthistory.domain;

import com.example.Ecommerce.interesthistory.dto.InterestHistoryCreateDto;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
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
@Table(name = "InterestHistory")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class InterestHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId", nullable = false)
  private Product product;

  @Column(nullable = false)
  private String productName;

  @Column(nullable = false)
  private Integer productSalePrice;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

  public static InterestHistory of(InterestHistoryCreateDto.Request request, User user,
      Product product) {
    InterestHistory interestHistory = new InterestHistory();
    interestHistory.setUser(user);
    interestHistory.setProduct(product);
    interestHistory.setProductName(product.getName());
    interestHistory.setProductSalePrice(product.getPrice());
    return interestHistory;
  }
}