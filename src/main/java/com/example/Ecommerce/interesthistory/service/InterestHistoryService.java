package com.example.Ecommerce.interesthistory.service;

import com.example.Ecommerce.interesthistory.dto.InterestHistoryCreateDto;
import com.example.Ecommerce.interesthistory.dto.InterestHistoryResponseDto;
import org.springframework.data.domain.Page;

public interface InterestHistoryService {

    InterestHistoryCreateDto.Response addInterest(Long userId, Long productId) throws Exception;

    void deleteInterest(Long interestHistoryId) throws Exception;

    Page<InterestHistoryResponseDto> getInterestHistories(Long userId, int page, int size);
}