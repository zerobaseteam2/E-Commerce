package com.example.Ecommerce.review.repository;

import com.example.Ecommerce.review.domain.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewCustomRepository {

  private final JPAQueryFactory query;

  @Override
  public Double findByStarsByProductId(Long productId) {
    QReview review = QReview.review;

    return query.select(review.stars.sum())
        .from(review)
        .where(review.product.id.eq(productId)).fetchFirst();
  }

  @Override
  public Long findByProductId(Long productId) {
    QReview review = QReview.review;

    return query.select(review.count())
        .from(review)
        .where(review.product.id.eq(productId)).fetchOne();
  }
}
