package com.example.Ecommerce.inquiry.service.impl;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.inquiry.dto.*;
import com.example.Ecommerce.inquiry.dto.RegisterInquiryDto.Request;
import com.example.Ecommerce.inquiry.dto.admin.RegisterInquiryReplyDto;
import com.example.Ecommerce.inquiry.dto.admin.UpdateInquiryReplyDto;
import com.example.Ecommerce.inquiry.dto.admin.ViewInquiryReplyDto;
import com.example.Ecommerce.inquiry.repository.InquiryReplyRepository;
import com.example.Ecommerce.inquiry.repository.InquiryRepository;
import com.example.Ecommerce.inquiry.service.InquiryService;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.Ecommerce.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryServiceImpl implements InquiryService {
  private final InquiryRepository inquiryRepository;
  private final InquiryReplyRepository inquiryReplyRepository;

  // 문의 작성
  @Override
  public RegisterInquiryDto.Response registerInquiry(RegisterInquiryDto.Request request,
      User user) {
    Inquiry inquiry = inquiryRepository.save(Request.toEntity(request, user));

    return RegisterInquiryDto.Response.toDto(inquiry);
  }

  // 문의 수정
  @Override
  @Transactional
  public UpdaterInquiryDto.Response updateInquiry(Long inquiryId, UpdaterInquiryDto.Request request, User user) {
    Inquiry nowInquiry = inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

    // 권한 확인
    if (!nowInquiry.getUser().getUserId().equals(user.getUserId()) && !user.getRole().equals(UserRole.ADMIN)) {
      throw new CustomException(NO_PERMISSION_TO_VIEW);
    }

    // 문의 답변이 달린 경우 내용 수정 불가
    if (nowInquiry.isReplyState()) {
      throw new CustomException(CONTENT_CAN_NOT_BE_MODIFIED);
    }

    nowInquiry.update(request);

    return UpdaterInquiryDto.Response.toDto(nowInquiry);
  }

  // 문의 취소(삭제)
  @Override
  public void deleteInquiry(Long inquiryId, User user) {
    Inquiry nowInquiry = inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

    // 권한 확인
    if (!nowInquiry.getUser().getUserId().equals(user.getUserId()) && !user.getRole().equals(UserRole.ADMIN)) {
      throw new CustomException(NO_PERMISSION_TO_VIEW);
    }

    inquiryRepository.deleteById(inquiryId);

    if (nowInquiry.isReplyState()) {
      // 문의 답변도 함께 삭제
      inquiryReplyRepository.deleteByInquiry_Id(inquiryId);
    }
  }

  // 문의 상세 조회
  @Override
  public ViewInquiryDto viewInquiry(Long inquiryId, User user) {
    Inquiry nowInquiry = inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

    // 권한 확인
    if (!nowInquiry.getUser().getUserId().equals(user.getUserId()) && !user.getRole().equals(UserRole.ADMIN)) {
      throw new CustomException(NO_PERMISSION_TO_VIEW);
    }

    if (nowInquiry.isReplyState()) {
      InquiryReply inquiryReply = inquiryReplyRepository.findByInquiry_Id(nowInquiry.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));
      ViewInquiryReplyDto replyDto = ViewInquiryReplyDto.toDto(inquiryReply);
      return ViewInquiryDto.toDto(nowInquiry, replyDto);
    }

    return ViewInquiryDto.toDto(nowInquiry);
  }

  // 본인이 작성한 문의 목록 조회
  @Override
  public InquiryPageDto viewInquiryList(int pageNo, User user) {
    // 정렬 기준에 따라 pageable 객체 초기화
    Pageable pageable = createPageable(pageNo);

    // 찾아온 데이터 Page 객체
    Page<Inquiry> inquiryPage = inquiryRepository.findAllByUser(pageable, user);

    // page 객체에서 쿠폰 리스트를 추출하여 responseDto에 저장
    List<InquiryListDto> responseDto = createListResponseDto(inquiryPage);

    // page 객체를 통해 return
    return new InquiryPageDto().toDto(pageNo, inquiryPage, responseDto);
  }

  private Pageable createPageable(int pageNo) {
    return PageRequest.of(pageNo, 10);
  }

  private List<InquiryListDto> createListResponseDto(Page<Inquiry> inquiryPage) {
    List<Inquiry> inquiries = inquiryPage.getContent();
    List<InquiryListDto> responseDto = new ArrayList<>();
    for (Inquiry inquiry : inquiries) {
      InquiryListDto inquiryListDto = InquiryListDto.toDto(inquiry);
      responseDto.add(inquiryListDto);
    }

    return responseDto;
  }

  // 관리자 답변 작성
  @Override
  @Transactional
  public ViewInquiryDto registerInquiryReply(RegisterInquiryReplyDto.Request request, User user) {
    Inquiry nowInquiry = inquiryRepository.findById(request.getInquiryId())
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

    // 이미 답변이 존재할 경우 예외처리
    if (nowInquiry.isReplyState()) {
      throw new CustomException(EXIST_REPLY);
    }

    InquiryReply inquiryReply = inquiryReplyRepository.save(
        RegisterInquiryReplyDto.Request.toEntity(request, nowInquiry, user));

    nowInquiry.addReply();

    ViewInquiryReplyDto replyDto = ViewInquiryReplyDto.toDto(inquiryReply);
    return ViewInquiryDto.toDto(nowInquiry, replyDto);
  }

  // 관리자 답변 수정
  @Override
  @Transactional
  public ViewInquiryDto updateInquiryReply(UpdateInquiryReplyDto.Request request, User user) {
    InquiryReply nowReply = inquiryReplyRepository.findById(request.getInquiryReplyId())
        .orElseThrow(() -> new CustomException(INQUIRY_REPLY_NOT_FOUND));

    if (!nowReply.getAdmin().getId().equals(user.getId())) {
      throw new CustomException(NO_PERMISSION_TO_UPDATE);
    }

    nowReply.updateReply(request);

    Long inquiryId = nowReply.getInquiry().getId();
    Inquiry nowInquiry = inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

    ViewInquiryReplyDto replyDto = ViewInquiryReplyDto.toDto(nowReply);
    return ViewInquiryDto.toDto(nowInquiry, replyDto);
  }

  // 관리자용 문의 목록 조회
  @Override
  public InquiryPageDto viewInquiryListForAdmin(int pageNo, User user, String filter) {
    // 관리자 권한 확인
    if (!user.getRole().equals(UserRole.ADMIN)) {
      throw new CustomException(NO_PERMISSION_TO_VIEW);
    }

    // 정렬 기준에 따라 pageable 객체 초기화
    Pageable pageable = createPageable(pageNo);

    Page<Inquiry> inquiryPage;
    // 찾아온 데이터 Page 객체
    if (filter.equals("NO_REPLY")) {
      inquiryPage = inquiryRepository.findAllByReplyState(pageable, false);
    } else {
      inquiryPage = inquiryRepository.findAll(pageable);
    }

    // page 객체에서 쿠폰 리스트를 추출하여 responseDto에 저장
    List<InquiryListDto> responseDto = createListResponseDto(inquiryPage);

    // page 객체를 통해 return
    return new InquiryPageDto().toDto(pageNo, inquiryPage, responseDto);
  }


}
