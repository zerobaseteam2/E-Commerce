package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.dto.seller.ProductDto;
import com.example.Ecommerce.product.dto.seller.page.ProductOptionPageResponse;
import com.example.Ecommerce.product.dto.seller.page.ProductPageResponse;
import com.example.Ecommerce.product.dto.seller.page.ProductTagPageResponse;
import com.example.Ecommerce.product.service.ProductService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  // 등록 요청한 상품 취소요청

  // 등록된 상품 수정하기

  // 등록된 상품중 판매중지한 물품 삭제하기

  // 판매 거절된 상품 내역 확인
  @GetMapping("/rejectList")
  public ResponseEntity<ProductPageResponse> getRejectList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getRejectList(sellerId, pageNo, pageSize));
  }

  // 내가 판매하는 상품 리스트 확인
  @GetMapping("/sellingList")
  public ResponseEntity<ProductPageResponse> getSellingList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getSellingList(sellerId, pageNo, pageSize));
  }

  // 내가 요청한 상품 옵션 리스트 확인
  @GetMapping("/optionList")
  public ResponseEntity<ProductOptionPageResponse> getOptionList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long productId,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getOptionList(sellerId, productId, pageNo, pageSize));
  }

  // 내가 요청한 상품 옵션 태그 리스트 확인
  @GetMapping("/tagList")
  public ResponseEntity<ProductTagPageResponse> getTagList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long productId,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getTagList(sellerId, productId, pageNo, pageSize));
  }

}
