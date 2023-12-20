package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.example.Ecommerce.product.domain.ProductTag;
import com.example.Ecommerce.product.domain.QProduct;
import com.example.Ecommerce.product.domain.QProductOption;
import com.example.Ecommerce.product.domain.QProductTag;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductCustomRepository {

  private final JPAQueryFactory query;
  QProduct product = QProduct.product;
  QProductOption productOption = QProductOption.productOption;
  QProductTag productTag = QProductTag.productTag;

  // 관리자 상품 요청 내역 확인
  @Override
  public Page<Product> findByConfirm(ProductConfirm confirm, Pageable pageable) {

    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(confirm))
        .orderBy(product.createAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(confirm))
        .fetch();

    int totalCount = products.size();

    return new PageImpl<>(results, pageable, totalCount);
  }

  // 판매자 승인 거절된 상품 내역 확인
  @Override
  public Page<Product> findByProductConfirm(ProductConfirm confirm, Long sellerId,
      Pageable pageable) {

    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(confirm))
        .where(product.sellerId.eq(sellerId))
        .orderBy(product.createAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(confirm))
        .where(product.sellerId.eq(sellerId))
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  @Override
  public Page<ProductOption> findByProductId(Long sellerId, Long productId, Pageable pageable) {

    List<ProductOption> results = query.selectFrom(productOption)
        .where(productOption.product.id.eq(productId))
        .where(productOption.product.sellerId.eq(sellerId))
        .orderBy(productOption.createAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<ProductOption> productOptions = query.selectFrom(productOption)
        .where(productOption.product.id.eq(productId))
        .where(productOption.product.sellerId.eq(sellerId))
        .fetch();

    int totalCount = productOptions.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  @Override
  public Page<ProductTag> findTagByProductId(Long sellerId, Long productId, Pageable pageable) {

    List<ProductTag> results = query.selectFrom(productTag)
        .where(productTag.product.id.eq(productId))
        .where(productTag.product.sellerId.eq(sellerId))
        .orderBy(productTag.createAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<ProductTag> productTags = query.selectFrom(productTag)
        .where(productTag.product.id.eq(productId))
        .where(productTag.product.sellerId.eq(sellerId))
        .fetch();

    int totalCount = productTags.size();
    return new PageImpl<>(results, pageable, totalCount);
  }
}
