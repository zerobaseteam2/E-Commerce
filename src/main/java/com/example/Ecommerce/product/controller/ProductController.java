package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.dto.ProductDto;
import com.example.Ecommerce.product.service.ProductService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/product")
public class ProductController {

  private final ProductService productService;

  // 상품 등록 요청
  @PostMapping("/register")
  public ResponseEntity<ProductDto> registerProduct(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid AddProductForm form) {
    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(ProductDto.from(productService.registerProduct(sellerId, form)));
  }


  // 등록 요청한 상품 취소하기

  // 등록된 상품 수정하기

  // 등록된 상품중 판매중지한 물품 삭제하기



}
