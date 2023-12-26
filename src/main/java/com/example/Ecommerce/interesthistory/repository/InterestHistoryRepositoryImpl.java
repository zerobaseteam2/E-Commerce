package com.example.Ecommerce.interesthistory.repository;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class InterestHistoryRepositoryImpl implements InterestHistoryCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<InterestHistory> findPagedInterestHistoriesByUserId(Long userId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // 조회 쿼리 생성
        CriteriaQuery<InterestHistory> cq = cb.createQuery(InterestHistory.class);
        Root<InterestHistory> interestHistory = cq.from(InterestHistory.class);
        cq.where(cb.equal(interestHistory.get("user").get("id"), userId));

        TypedQuery<InterestHistory> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<InterestHistory> result = query.getResultList();

        // COUNT 쿼리 생성
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<InterestHistory> countRoot = countQuery.from(InterestHistory.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.equal(countRoot.get("user").get("id"), userId));

        Long totalRows = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(result, pageable, totalRows);
    }
}