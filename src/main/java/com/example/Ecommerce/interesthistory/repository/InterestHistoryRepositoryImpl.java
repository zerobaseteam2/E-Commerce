package com.example.Ecommerce.interesthistory.repository;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class InterestHistoryRepositoryImpl implements InterestHistoryCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<InterestHistory> findPagedInterestHistoriesByUserId(Long userId, Pageable pageable) {
        // 커스텀 쿼리 로직을 구현합니다. 예를 들어:
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InterestHistory> cq = cb.createQuery(InterestHistory.class);
        Root<InterestHistory> interestHistory = cq.from(InterestHistory.class);

        cq.where(cb.equal(interestHistory.get("user").get("id"), userId));
        TypedQuery<InterestHistory> query = entityManager.createQuery(cq);

        // 페이징 처리
        int totalRows = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<InterestHistory> result = query.getResultList();

        return new PageImpl<>(result, pageable, totalRows);
    }
}