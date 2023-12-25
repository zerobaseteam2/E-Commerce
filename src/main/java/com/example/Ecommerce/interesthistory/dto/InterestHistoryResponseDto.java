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

    private Long id; // 찜한 내역의 고유 ID
    private String productName; // 상품 이름
    private Integer productSalePrice; // 상품 판매 가격
    private Long userId; // 사용자 ID
    private Long productId; // 상품 ID

    // 엔티티를 DTO로 변환
    public static InterestHistoryResponseDto toDto(InterestHistory entity) {
        return InterestHistoryResponseDto.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .productSalePrice(entity.getProductSalePrice())
                .userId(entity.getUser().getId())
                .productId(entity.getProduct().getId())
                .build();
    }

    // 페이지 객체를 반환
    public static Page<InterestHistoryResponseDto> toDtoPage(Page<InterestHistory> entityPage) {
        return entityPage.map(InterestHistoryResponseDto::toDto);
    }
}
