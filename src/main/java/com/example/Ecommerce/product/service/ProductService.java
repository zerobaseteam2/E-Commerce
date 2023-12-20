package com.example.Ecommerce.product.service;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.example.Ecommerce.product.domain.ProductTag;
import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import com.example.Ecommerce.product.dto.seller.ProductListDto;
import com.example.Ecommerce.product.dto.seller.ProductOptionListDto;
import com.example.Ecommerce.product.dto.seller.ProductTagListDto;
import com.example.Ecommerce.product.dto.seller.page.ProductOptionPageResponse;
import com.example.Ecommerce.product.dto.seller.page.ProductPageResponse;
import com.example.Ecommerce.product.dto.seller.page.ProductTagPageResponse;
import com.example.Ecommerce.product.repository.ProductCustomRepository;
import com.example.Ecommerce.product.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductCustomRepository productCustomRepository;

  // 상품 등록 요청
  public Product registerProduct(Long sellerId, AddProductForm form) {
    return productRepository.save(Product.of(sellerId, form));
  }

  // 판매 거절된 상품 내역 확인
  public ProductPageResponse getRejectList(Long sellerId, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Product> productPage = productCustomRepository.findByProductConfirm(
        ProductConfirm.NOT_APPROVED, sellerId, pageable);
    List<Product> productList = productPage.getContent();

    return ProductPageResponse.builder()
        .productList(
            productList.stream().map(ProductListDto::from).collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }

  // 내가 판매하는 상품 리스트 확인
  public ProductPageResponse getSellingList(Long sellerId, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Product> productPage = productCustomRepository.findByProductConfirm(
        ProductConfirm.APPROVED, sellerId, pageable);
    List<Product> productList = productPage.getContent();

    if (productList.size() == 0) {
      throw new CustomException(ErrorCode.SELLING_PRODUCT_NOT_EXIST);
    }

    return ProductPageResponse.builder()
        .productList(
            productList.stream().map(ProductListDto::getList).collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }

  // 내가 판매하는 상품 혹은 대기중 이거나 취소된 옵션 리스트 확인
  public ProductOptionPageResponse getOptionList(Long sellerId, Long productId, int pageNo,
      int pageSize) {

    Product product = productRepository.findProductByIdAndSellerId(productId, sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<ProductOption> productOptionPage = productCustomRepository.findByProductId(
        sellerId, productId, pageable);
    List<ProductOption> productOptionList = productOptionPage.getContent();

    return ProductOptionPageResponse.builder()
        .productOptionList(
            productOptionList.stream()
                .map(productOption -> ProductOptionListDto.from(product, productOption))
                .collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productOptionPage.getTotalElements())
        .totalPages(productOptionPage.getTotalPages())
        .last(productOptionPage.isLast())
        .build();
  }

  // 내가 판매하는 상품 옵션 태그 확인
  public ProductTagPageResponse getTagList(Long sellerId, Long productId, int pageNo,
      int pageSize) {

    Product product = productRepository.findProductByIdAndSellerId(productId, sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<ProductTag> productTagPage = productCustomRepository.findTagByProductId(
        sellerId, productId, pageable);

    List<ProductTag> productTagList = productTagPage.getContent();

    return ProductTagPageResponse.builder()
        .tagList(
            productTagList.stream()
                .map(productTag -> ProductTagListDto.from(product, productTag))
                .collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productTagPage.getTotalElements())
        .totalPages(productTagPage.getTotalPages())
        .last(productTagPage.isLast())
        .build();
  }

  // 요청한 상품 취소하기

  // 등록된 상품 수정하기

  // 등록된 상품중 판매중지한 물품 삭제하기

}
