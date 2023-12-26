package com.example.Ecommerce.interesthistory.dto;

import com.example.Ecommerce.interesthistory.domain.InterestHistory;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.user.domain.User;
import lombok.*;

public class InterestHistoryCreateDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private Long userId;
        private Long productId;

        public static InterestHistory toEntity(Request request, User user, Product product) {
            return InterestHistory.builder()
                    .user(user)
                    .product(product)
                    .productName(product.getName())
                    .productSalePrice(product.getPrice())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long userId;
        private Long productId;
        private String productName;
        private Integer productSalePrice;

        public static InterestHistoryCreateDto.Response toDto(InterestHistory interestHistory) {
            return Response.builder()
                    .userId(interestHistory.getUser().getId())
                    .productId(interestHistory.getProduct().getId())
                    .productName(interestHistory.getProductName())
                    .productSalePrice(interestHistory.getProductSalePrice())
                    .build();
        }
    }
}

