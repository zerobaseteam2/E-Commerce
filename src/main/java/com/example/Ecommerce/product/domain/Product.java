package com.example.Ecommerce.product.domain;

import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.dto.ProductConfirm;
import com.example.Ecommerce.product.dto.ProductState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
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
  private String origin;

  // 판매등록 승인 여부
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductConfirm confirm;

  // 상품 할인 여부
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductState discount;

  // 상품 상태
  @Enumerated(EnumType.STRING)
  private ProductState state;

  // 상품 옵션 리스트
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  private List<ProductOption> productOptionList = new ArrayList<>();

  // 상품 태그 리스트
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  private List<ProductTag> productTags = new ArrayList<>();

  public static Product of(Long sellerId, AddProductForm form) {

    return Product.builder()
        .sellerId(sellerId)
        .category(form.getCategory())
        .name(form.getName())
        .price(form.getPrice())
        .description(form.getDescription())
        .origin(form.getOrigin())
        .confirm(ProductConfirm.WAITING)
        .discount(form.getDiscount())
        .productOptionList(form.getOptionFormList().stream()
            .map(productOptionForm -> ProductOption.of(sellerId, productOptionForm))
            .collect(Collectors.toList()))
        .productTags(form.getTagList().stream()
            .map(productTagForm -> ProductTag.of(sellerId, productTagForm))
            .collect(Collectors.toList()))
        .build();
  }
}
