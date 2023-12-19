package com.example.Ecommerce.product.repository;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.example.Ecommerce.product.domain.ProductTag;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

  Page<Product> findByConfirm(ProductConfirm confirm, Pageable pageable);

  Page<Product> findByProductConfirm(ProductConfirm confirm, Long sellerId, Pageable pageable);

  Page<ProductOption> findByProductId(Long sellerId, Long productId, Pageable pageable);

  Page<ProductTag> findTagByProductId(Long sellerId, Long productId, Pageable pageable);

}