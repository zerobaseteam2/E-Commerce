package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
