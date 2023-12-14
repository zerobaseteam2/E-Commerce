package com.example.Ecommerce.product.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
@Audited
@AuditOverride(forClass = BaseEntity.class)
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long sellerId;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private String description;

  // 원산지
  @Column(nullable = true)
  private String origin;

  // 판매등록 승인 여부
  @Column(nullable = false)
  private boolean confirm;

  // 상품 할인 여부
  @Column(nullable = false)
  private boolean discount;

  // 상품 상태
  @Column(nullable = false)
  private boolean state;

  // 상품 옵션 리스트
  @OneToMany(cascade = CascadeType.ALL)
  private List<ProductOption> productOptionList = new ArrayList<>();

  // 상품 태그 리스트
  @OneToMany(cascade = CascadeType.ALL)
  private List<ProductTag> productTags = new ArrayList<>();

}
