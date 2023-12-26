package com.example.Ecommerce.product.domain;

import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import com.example.Ecommerce.product.dto.seller.ProductState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

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

  // 상품 별점 - 리뷰에서 사람들의 별점 평균별점
  private Double stars;

  // 상품 할인 여부
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductState discount;

  // 상품 상태
  @Enumerated(EnumType.STRING)
  private ProductState state;

  // 상품 승인 요청 거절사유 - 승인된 물품은 null 값을 유지
  private String reason;

  // 상품 옵션 리스트
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  @Builder.Default
  private List<ProductOption> productOptionList = new ArrayList<>();

  // 상품 태그 리스트
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  @Builder.Default
  private List<ProductTag> productTags = new ArrayList<>();


  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

  //찜 상품 내역
//  @OneToMany(cascade = CascadeType.ALL)
//  @JoinColumn(name = "product_id")
//  private List<InterestHistory> interestHistories = new ArrayList<>();


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
        .stars(0.0)
        .productOptionList(form.getOptionFormList().stream()
            .map(productOptionForm -> ProductOption.of(sellerId, productOptionForm))
            .collect(Collectors.toList()))
        .productTags(form.getTagList().stream()
            .map(productTagForm -> ProductTag.of(sellerId, productTagForm))
            .collect(Collectors.toList()))
        .build();
  }
}
