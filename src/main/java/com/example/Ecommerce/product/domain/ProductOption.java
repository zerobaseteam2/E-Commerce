package com.example.Ecommerce.product.domain;

import com.example.Ecommerce.product.domain.form.AddProductOptionForm;
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
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class ProductOption extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long sellerId;

  @Column(nullable = false)
  private Integer count;

  @Column(nullable = false)
  private String optionName;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;


  public static ProductOption of(Long sellerId, AddProductOptionForm form) {

    return ProductOption.builder()
        .sellerId(sellerId)
        .count(form.getCount())
        .optionName(form.getOptionName())
        .build();
  }
  public void reduceInventory(Integer quantity) {

  }
}
