package com.example.Ecommerce.product.service;

import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  // 상품 등록 요청
  public Product registerProduct(Long sellerId, AddProductForm form) {
    return productRepository.save(Product.of(sellerId, form));
  }

}