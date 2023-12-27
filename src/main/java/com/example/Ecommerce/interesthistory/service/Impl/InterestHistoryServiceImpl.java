package com.example.Ecommerce.interesthistory.service.Impl;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import com.example.Ecommerce.interesthistory.dto.InterestHistoryCreateDto;
import com.example.Ecommerce.interesthistory.dto.InterestHistoryResponseDto;
import com.example.Ecommerce.interesthistory.repository.InterestHistoryRepository;
import com.example.Ecommerce.interesthistory.service.InterestHistoryService;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.repository.ProductRepository;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InterestHistoryServiceImpl implements InterestHistoryService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InterestHistoryRepository interestHistoryRepository;

    @Autowired
    public InterestHistoryServiceImpl(UserRepository userRepository,
                                      ProductRepository productRepository,
                                      InterestHistoryRepository interestHistoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.interestHistoryRepository = interestHistoryRepository;
    }


    @Override
    public InterestHistoryCreateDto.Response addInterest(Long userId, Long productId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));

        interestHistoryRepository.findByUserIdAndProductId(userId, productId).ifPresent(interestHistory -> {
            throw new RuntimeException(new Exception("Product already in interest list"));
        });

        InterestHistoryCreateDto.Request interestRequest = new InterestHistoryCreateDto.Request(userId, productId);
        InterestHistory interestHistory = InterestHistoryCreateDto.Request.toEntity(interestRequest, user, product);
        interestHistoryRepository.save(interestHistory);

        return InterestHistoryCreateDto.Response.toDto(interestHistory);
    }



    @Override
    public void deleteInterest(Long interestHistoryId) throws Exception {
        InterestHistory interestHistory = interestHistoryRepository.findById(interestHistoryId)
                .orElseThrow(() -> new Exception("Interest history not found"));

        interestHistoryRepository.delete(interestHistory);
    }

    @Override
    public Page<InterestHistoryResponseDto> getInterestHistories(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<InterestHistory> interestHistories = interestHistoryRepository.findPagedInterestHistoriesByUserId(userId, pageable);

        return InterestHistoryResponseDto.toDtoPage(interestHistories);
    }
}