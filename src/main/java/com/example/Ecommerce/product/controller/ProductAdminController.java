package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.product.domain.form.admin.ProductProcessForm;
import com.example.Ecommerce.product.dto.admin.ProductProcessDto;
import com.example.Ecommerce.product.dto.admin.WaitingListResponse;
import com.example.Ecommerce.product.service.ProductAdminService;
import com.example.Ecommerce.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("admin/product")
public class ProductAdminController {

  private final ProductAdminService productAdminService;

  // 승인 대기중인 상품 내역 가져와서 보기
  @GetMapping("/waitingList")
  public ResponseEntity<WaitingListResponse> getList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    // WAITING 값인 상품 목록 전부 가져오기
    return ResponseEntity.ok(productAdminService.getWaitingList(pageNo, pageSize));
  }

  // 요청 승인 및 거절
  @PostMapping("/process")
  public ResponseEntity<ProductProcessDto> process(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProductProcessForm form) {

    return ResponseEntity.ok(productAdminService.process(form));
  }

  // 승인된 내역 상품 보기

  // 승인 거절한 상품 보기


}
