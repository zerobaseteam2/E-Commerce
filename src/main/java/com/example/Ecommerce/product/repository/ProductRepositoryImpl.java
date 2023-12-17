package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.QProduct;
import com.example.Ecommerce.product.dto.ProductConfirm;
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
}
