package com.example.Ecommerce.interesthistory.dto;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestHistoryResponseDto {

    private Long id; // 찜내역의 ID
    private String productName;
    private Integer productSalePrice;
    private Long userId;
    private Long productId;

    public static InterestHistoryResponseDto toDto(InterestHistory entity) {
        return InterestHistoryResponseDto.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .productSalePrice(entity.getProductSalePrice())
                .userId(entity.getUser().getId())
                .productId(entity.getProduct().getId())
                .build();
    }

    public static Page<InterestHistoryResponseDto> toDtoPage(Page<InterestHistory> entityPage) {
        return entityPage.map(InterestHistoryResponseDto::toDto);
    }
}
