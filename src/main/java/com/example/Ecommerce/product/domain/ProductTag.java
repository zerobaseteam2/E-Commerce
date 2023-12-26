package com.example.Ecommerce.product.domain;

import com.example.Ecommerce.product.domain.form.AddProductTagForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class ProductTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long sellerId;

  @Column(nullable = false)
  private String tagName;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

  public static ProductTag of(Long sellerId, AddProductTagForm form) {
    return ProductTag.builder()
        .sellerId(sellerId)
        .tagName(form.getTagName())
        .build();
  }
}
