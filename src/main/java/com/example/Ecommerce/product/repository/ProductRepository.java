package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findProductByIdAndSellerId(Long sellerId, Long id);
}
