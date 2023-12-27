package com.example.Ecommerce.interesthistory.repository;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterestHistoryCustomRepository {
    Page<InterestHistory> findPagedInterestHistoriesByUserId(Long userId, Pageable pageable);
}
