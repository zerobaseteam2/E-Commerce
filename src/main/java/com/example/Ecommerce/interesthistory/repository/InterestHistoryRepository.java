package com.example.Ecommerce.interesthistory.repository;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestHistoryRepository extends JpaRepository<InterestHistory, Long>, InterestHistoryCustomRepository {
    Page<InterestHistory> findPagedInterestHistoriesByUserId(Long userId, Pageable pageable);

    Optional<InterestHistory> findByUserIdAndProductId(Long userId, Long productId);

}