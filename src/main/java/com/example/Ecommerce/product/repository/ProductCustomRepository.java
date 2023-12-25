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

  Page<Product> findProductsByTagOrderByModifiedAtDesc(String tagName, Pageable pageable);

  Page<Product> findProductsByTagOrderByModifiedAtAsc(String tagName, Pageable pageable);

  Page<Product> findProductsByTagOrderByPriceDesc(String tagName, Pageable pageable);

  Page<Product> findProductsByTagOrderByPriceAsc(String tagName, Pageable pageable);

  Page<Product> findProductsByTagOrderByReview(String tagName, Pageable pageable);

  Page<Product> findProductsByTagOrderByStarsDesc(String tagName, Pageable pageable);

  Page<Product> findProductsByTagOrderByStarsAsc(String tagName, Pageable pageable);

  Page<Product> findProductOrderByModifiedAtDesc(String word, Pageable pageable);

  Page<Product> findProductOrderByModifiedAtAsc(String word, Pageable pageable);

  Page<Product> findProductOrderByPriceDesc(String word, Pageable pageable);

  Page<Product> findProductOrderByPriceAsc(String word, Pageable pageable);

  Page<Product> findProductsOrderByReview(String word, Pageable pageable);

  Page<Product> findProductOrderByStarsDesc(String word, Pageable pageable);

  Page<Product> findProductOrderByStarsAsc(String word, Pageable pageable);

}
