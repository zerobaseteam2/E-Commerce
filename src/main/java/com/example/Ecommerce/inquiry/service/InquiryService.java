package com.example.Ecommerce.inquiry.service;

import com.example.Ecommerce.inquiry.dto.InquiryPageDto;
import com.example.Ecommerce.inquiry.dto.RegisterInquiryDto;
import com.example.Ecommerce.inquiry.dto.UpdaterInquiryDto;
import com.example.Ecommerce.inquiry.dto.ViewInquiryDto;
import com.example.Ecommerce.inquiry.dto.admin.RegisterInquiryReplyDto;
import com.example.Ecommerce.inquiry.dto.admin.UpdateInquiryReplyDto;
import com.example.Ecommerce.user.domain.User;

public interface InquiryService {
  RegisterInquiryDto.Response registerInquiry(RegisterInquiryDto.Request request,
      User user);

  // 문의 수정
  UpdaterInquiryDto.Response updateInquiry(Long inquiryId, UpdaterInquiryDto.Request request, User user);

  // 문의 취소(삭제)
  void deleteInquiry(Long inquiryId, User user);

  // 문의 상세 조회
  ViewInquiryDto viewInquiry(Long inquiryId, User user);

  // 본인이 작성한 문의 목록 조회
  InquiryPageDto viewInquiryList(int pageNo, User user);
  
  // 관리자 답변 작성
  ViewInquiryDto registerInquiryReply(RegisterInquiryReplyDto.Request request, User user);

  // 관리자 답변 수정
  ViewInquiryDto updateInquiryReply(UpdateInquiryReplyDto.Request request, User user);

  // 관리자용 문의 목록 조회
  InquiryPageDto viewInquiryListForAdmin(int pageNo, User user, String filter);
}
