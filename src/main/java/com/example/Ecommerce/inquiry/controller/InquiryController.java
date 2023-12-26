package com.example.Ecommerce.inquiry.controller;

import com.example.Ecommerce.inquiry.dto.InquiryPageDto;
import com.example.Ecommerce.inquiry.dto.RegisterInquiryDto;
import com.example.Ecommerce.inquiry.dto.UpdaterInquiryDto;
import com.example.Ecommerce.inquiry.dto.ViewInquiryDto;
import com.example.Ecommerce.inquiry.dto.admin.RegisterInquiryReplyDto;
import com.example.Ecommerce.inquiry.dto.admin.UpdateInquiryReplyDto;
import com.example.Ecommerce.inquiry.service.InquiryService;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

  private final InquiryService inquiryService;

  @PostMapping("/register")
  public ResponseEntity<RegisterInquiryDto.Response> registerInquiry(
      @RequestBody @Valid RegisterInquiryDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    RegisterInquiryDto.Response response = inquiryService.registerInquiry(request, user);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{inquiryId}")
  public ResponseEntity<UpdaterInquiryDto.Response> updateInquiry(
      @PathVariable Long inquiryId,
      @RequestBody @Valid UpdaterInquiryDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    UpdaterInquiryDto.Response response = inquiryService.updateInquiry(inquiryId, request, user);
    return ResponseEntity.ok(response);
  }
  @DeleteMapping("/delete/{inquiryId}")
  public ResponseEntity<?> deleteInquiry(
      @PathVariable Long inquiryId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    inquiryService.deleteInquiry(inquiryId, user);
    return ResponseEntity.ok("");
  }
  @GetMapping("/view/{inquiryId}")
  public ResponseEntity<ViewInquiryDto> viewInquiry(
      @PathVariable Long inquiryId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    ViewInquiryDto response = inquiryService.viewInquiry(inquiryId, user);
    return ResponseEntity.ok(response);
  }
  @GetMapping("/viewList")
  public ResponseEntity<InquiryPageDto> viewInquiryList(
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    InquiryPageDto response = inquiryService.viewInquiryList(pageNo, user);
    return ResponseEntity.ok(response);
  }
  @PostMapping("/register-reply")
  public ResponseEntity<ViewInquiryDto> registerInquiryReply(
      @RequestBody @Valid RegisterInquiryReplyDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    ViewInquiryDto response = inquiryService.registerInquiryReply(request, user);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update-reply")
  public ResponseEntity<ViewInquiryDto> updateInquiryReply(
      @RequestBody @Valid UpdateInquiryReplyDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    ViewInquiryDto response = inquiryService.updateInquiryReply(request, user);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/viewList/admin")
  public ResponseEntity<InquiryPageDto> viewInquiryListForAdmin(
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "filter", defaultValue = "ALL", required = false) String filter,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userDetails.getUser();
    InquiryPageDto response = inquiryService.viewInquiryListForAdmin(pageNo, user, filter);
    return ResponseEntity.ok(response);
  }
}
