package com.example.Ecommerce.product.domain;

import com.example.Ecommerce.product.domain.form.AddProductTagForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class ProductTag extends BaseEntity {

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

  public static ProductTag of(Long sellerId, AddProductTagForm form) {
    return ProductTag.builder()
        .id(form.getProductId())
        .sellerId(sellerId)
        .tagName(form.getTagName())
        .build();
  }
}
