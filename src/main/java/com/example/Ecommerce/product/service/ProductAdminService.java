package com.example.Ecommerce.product.service;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.form.CancelProductForm;
import com.example.Ecommerce.product.domain.form.admin.ProductProcessForm;
import com.example.Ecommerce.product.dto.admin.ListResponse;
import com.example.Ecommerce.product.dto.admin.ProductProcessDto;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import com.example.Ecommerce.product.dto.seller.ProductDto;
import com.example.Ecommerce.product.dto.seller.ProductState;
import com.example.Ecommerce.product.repository.ProductCustomRepository;
import com.example.Ecommerce.product.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductAdminService {

  private final ProductCustomRepository productCustomRepository;
  private final ProductRepository productRepository;

  // 요청 승인 대기중인 상품, 승인한 상품, 등록 취소 대기중인 상품, 등록 취소한 상품, 거절한 상품 내역 가져오기
  public ListResponse getList(ProductConfirm confirm, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Product> productPage = productCustomRepository.findByConfirm(confirm,pageable);
    List<Product> products = productPage.getContent();

    return ListResponse.builder()
        .productList(
            products.stream().map(ProductDto::exceptOptionTags).collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }

  // 요청 승인 및 거절 처리
  @Transactional
  public ProductProcessDto process(ProductProcessForm form) {

    Long productId = form.getProductId();
    Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(
        ErrorCode.PRODUCT_NOT_FOUND));

    // 해당 상품 상태가 WAITING이 아닐때 exception
    if (!product.getConfirm().equals(ProductConfirm.WAITING)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_WAITING);
    }
    // 승인시 상품 등록 approved 변경 및 state 변경
    if (form.getConfirm().equals(ProductConfirm.APPROVED)) {
      product.setConfirm(form.getConfirm());
      // 할인중
      if (product.getDiscount().equals(ProductState.ON_SALE)) {
        product.setState(ProductState.ON_SALE);
      }
      // 일반 판매중
      if (product.getDiscount().equals(ProductState.FOR_SALE)) {
        product.setState(ProductState.FOR_SALE);
      }
    }
    // 승인 거절시 approved 변경
    if (form.getConfirm().equals(ProductConfirm.NOT_APPROVED)) {
      product.setConfirm(form.getConfirm());
      product.setReason(form.getReason());
    }

    return ProductProcessDto.builder()
        .productId(product.getId())
        .name(product.getName())
        .category(product.getCategory())
        .price(product.getPrice())
        .description(product.getDescription())
        .origin(product.getOrigin())
        .confirm(product.getConfirm())
        .discount(product.getDiscount())
        .state(product.getState())
        .reason(product.getReason())
        .stars(product.getStars())
        .build();
  }

  // 판매 중지 및 등록 취소 요청 승인
  @Transactional
  public String cancelApprove(CancelProductForm form) {
    Long productId = form.getProductId();
    Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(
        ErrorCode.PRODUCT_NOT_FOUND));

    if (product.getConfirm().equals(ProductConfirm.REQUEST_CANCEL_REGISTER)) {
      product.setConfirm(ProductConfirm.APPROVED_CANCEL_REGISTER);
      return "등록 취소 요청 승인하셨습니다.";
    }
    if (product.getConfirm().equals(ProductConfirm.REQUEST_CANCEL_SELLING)) {
      product.setConfirm(ProductConfirm.APPROVED_CANCEL_SELLING);
      return "판매 중지 요청 승인하셨습니다.";
    }
    throw new CustomException(ErrorCode.PRODUCT_NOT_CANCEL_REQUEST);
  }

}
