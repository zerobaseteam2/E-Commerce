package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.dto.ProductConfirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

  Page<Product> findByConfirm(ProductConfirm confirm, Pageable pageable);

}
