package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.example.Ecommerce.product.domain.ProductTag;
import com.example.Ecommerce.product.domain.QProduct;
import com.example.Ecommerce.product.domain.QProductOption;
import com.example.Ecommerce.product.domain.QProductTag;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import com.example.Ecommerce.review.domain.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
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

  // 태그로 검색 최신순
  @Override
  public Page<Product> findProductsByTagOrderByModifiedAtDesc(String tagName, Pageable pageable) {

    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(productTag.createAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(productTag.createAt.desc())
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  // 태그로 검색 오래된순
  @Override
  public Page<Product> findProductsByTagOrderByModifiedAtAsc(String tagName, Pageable pageable) {

    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(productTag.createAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(productTag.createAt.asc())
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  // 태그로 검색 높은 가격순
  @Override
  public Page<Product> findProductsByTagOrderByPriceDesc(String tagName, Pageable pageable) {
    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.price.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.price.desc())
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  // 태그로 검색 낮은 가격순
  @Override
  public Page<Product> findProductsByTagOrderByPriceAsc(String tagName, Pageable pageable) {
    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.price.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.price.asc())
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  // 태그로 검색 리뷰 개수 높은순, 낮은순
  @Override
  public Page<Product> findProductsByTagOrderByReview(String tagName, Pageable pageable) {
    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  // 태그로 검색 높은 별점순
  @Override
  public Page<Product> findProductsByTagOrderByStarsDesc(String tagName, Pageable pageable) {
    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.stars.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.stars.desc())
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }

  @Override
  public Page<Product> findProductsByTagOrderByStarsAsc(String tagName, Pageable pageable) {
    List<Product> results = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.stars.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Product> products = query.selectFrom(product)
        .where(product.confirm.eq(ProductConfirm.APPROVED))
        .join(product.productTags, productTag)
        .where(productTag.tagName.contains(tagName))
        .orderBy(product.stars.asc())
        .fetch();

    int totalCount = products.size();
    return new PageImpl<>(results, pageable, totalCount);
  }
}
