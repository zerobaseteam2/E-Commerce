package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.domain.form.AddProductOptionForm;
import com.example.Ecommerce.product.domain.form.AddProductTagForm;
import com.example.Ecommerce.product.domain.form.CancelProductForm;
import com.example.Ecommerce.product.domain.form.UpdateProductForm;
import com.example.Ecommerce.product.domain.form.UpdateProductOptionForm;
import com.example.Ecommerce.product.domain.form.UpdateProductTagForm;
import com.example.Ecommerce.product.dto.seller.ProductDto;
import com.example.Ecommerce.product.dto.seller.ProductOptionDto;
import com.example.Ecommerce.product.dto.seller.ProductTagDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/product")
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

  // 판매승인된 상품에 옵션 추가
  @PostMapping("/post-Option")
  public ResponseEntity<ProductDto> addOption(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid AddProductOptionForm form) {
    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(ProductDto.from(productService.addOption(sellerId, form)));
  }

  // 판매승인된 상품에 태그 추가
  @PostMapping("/post-tag")
  public ResponseEntity<ProductDto> addTag(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid AddProductTagForm form) {
    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(ProductDto.from(productService.addTag(sellerId, form)));
  }

  // 등록된 상품 수정하기(옵션 및 태그도 같이 수정가능)
  @PutMapping
  public ResponseEntity<ProductDto> updateProduct(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody UpdateProductForm form) {
    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(ProductDto.from(productService.updateProduct(sellerId, form)));
  }

  // 등록된 상품의 옵션만 수정하기
  @PutMapping("/option")
  public ResponseEntity<ProductOptionDto> updateProductOption(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody UpdateProductOptionForm form) {
    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(ProductOptionDto.from(productService.updateOption(sellerId, form)));
  }

  // 등록된 상품의 태그만 수정하기
  @PutMapping("/tag")
  public ResponseEntity<ProductTagDto> updateProductTag(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody UpdateProductTagForm form) {
    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(ProductTagDto.from(productService.updateTag(sellerId, form)));
  }

  // 등록된 상품중 판매중지 혹은 등록 취소된 물품 삭제하기 - 연관된 모든 태그, 옵션들도 같이 삭제됨
  @DeleteMapping
  public ResponseEntity<String> deleteProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long productId) {
    Long sellerId = userDetails.getUser().getId();
    productService.deleteProduct(sellerId, productId);
    return ResponseEntity.ok("삭제되었습니다.");
  }

  // 등록승인된 상품의 옵션 삭제하기
  @DeleteMapping("/option")
  public ResponseEntity<String> deleteProductOption(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long optionId) {
    Long sellerId = userDetails.getUser().getId();
    productService.deleteProductOption(sellerId, optionId);
    return ResponseEntity.ok("삭제되었습니다.");
  }

  // 등록승인된 상품의 태그 삭제하기
  @DeleteMapping("/tag")
  public ResponseEntity<String> deleteProductTag(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long tagId) {
    Long sellerId = userDetails.getUser().getId();
    productService.deleteProductTag(sellerId, tagId);
    return ResponseEntity.ok("삭제되었습니다.");
  }


  // 등록 요청한 상품 취소요청
  @PostMapping("/cancelProduct")
  public ResponseEntity<String> cancelProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody CancelProductForm form) {
    Long sellerId = userDetails.getUser().getId();

    return ResponseEntity.ok(productService.cancelProduct(sellerId, form));
  }

  // 판매중인 상품 판매중지 요청
  @PostMapping("/cancelSellProduct")
  public ResponseEntity<String> cancelSellingProduct(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody CancelProductForm form) {
    Long sellerId = userDetails.getUser().getId();

    return ResponseEntity.ok(productService.cancelSellingProduct(sellerId, form));
  }


  // 판매 거절된 상품 내역 확인
  @GetMapping("/reject")
  public ResponseEntity<ProductPageResponse> getRejectList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getRejectList(sellerId, pageNo, pageSize));
  }

  // 내가 판매하는 상품 리스트 확인
  @GetMapping("/selling")
  public ResponseEntity<ProductPageResponse> getSellingList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getSellingList(sellerId, pageNo, pageSize));
  }

  // 내가 요청한 상품 옵션 리스트 확인
  @GetMapping("/option")
  public ResponseEntity<ProductOptionPageResponse> getOptionList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long productId,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getOptionList(sellerId, productId, pageNo, pageSize));
  }

  // 내가 요청한 상품 옵션 태그 리스트 확인
  @GetMapping("/tag")
  public ResponseEntity<ProductTagPageResponse> getTagList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long productId,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    Long sellerId = userDetails.getUser().getId();
    return ResponseEntity.ok(productService.getTagList(sellerId, productId, pageNo, pageSize));
  }

}
