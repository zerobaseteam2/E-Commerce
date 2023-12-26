package com.example.Ecommerce.product.repository;


import com.example.Ecommerce.product.domain.ProductTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {

  Optional<ProductTag> findBySellerIdAndId(Long sellerId, Long tagId);

}
