package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.ProductOption;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

  Optional<ProductOption> findBySellerIdAndId(Long sellerId, Long optionId);
}
