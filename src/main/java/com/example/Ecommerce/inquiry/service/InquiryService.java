package com.example.Ecommerce.inquiry.service;

import static com.example.Ecommerce.exception.ErrorCode.INQUIRY_REPLY_NOT_FOUND;
import static com.example.Ecommerce.exception.ErrorCode.NO_PERMISSION_TO_UPDATE;
import static com.example.Ecommerce.exception.ErrorCode.NO_PERMISSION_TO_VIEW;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.inquiry.dto.InquiryListDto;
import com.example.Ecommerce.inquiry.dto.InquiryPageDto;
import com.example.Ecommerce.inquiry.dto.RegisterInquiryDto;
import com.example.Ecommerce.inquiry.dto.RegisterInquiryDto.Request;
import com.example.Ecommerce.inquiry.dto.UpdaterInquiryDto;
import com.example.Ecommerce.inquiry.dto.ViewInquiryDto;
import com.example.Ecommerce.inquiry.dto.admin.RegisterInquiryReplyDto;
import com.example.Ecommerce.inquiry.dto.admin.UpdateInquiryReplyDto;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

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
