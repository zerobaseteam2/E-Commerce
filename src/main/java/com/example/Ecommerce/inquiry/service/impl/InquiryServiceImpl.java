package com.example.Ecommerce.inquiry.service.impl;

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
import com.example.Ecommerce.inquiry.repository.InquiryReplyRepository;
import com.example.Ecommerce.inquiry.repository.InquiryRepository;
import com.example.Ecommerce.inquiry.service.InquiryService;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
    if (!nowInquiry.getUser().equals(user) && !user.getRole().equals(UserRole.ADMIN)) {
      throw new CustomException(NO_PERMISSION_TO_VIEW);
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
    if (!nowInquiry.getUser().equals(user) && !user.getRole().equals(UserRole.ADMIN)) {
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
    if (!nowInquiry.getUser().equals(user) && !user.getRole().equals(UserRole.ADMIN)) {
      throw new CustomException(NO_PERMISSION_TO_VIEW);
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
    return PageRequest.of(pageNo, 10, Sort.by("issuanceDate").descending());
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
  public ViewInquiryDto registerInquiryReply(RegisterInquiryReplyDto.Request request, User user) {
    Inquiry nowInquiry = inquiryRepository.findById(request.getInquiryId())
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));
    InquiryReply inquiryReply = inquiryReplyRepository.save(
        RegisterInquiryReplyDto.Request.toEntity(request, nowInquiry, user));

    nowInquiry.addReply(inquiryReply);

    return ViewInquiryDto.toDto(nowInquiry);
  }

  // 관리자 답변 수정
  @Override
  @Transactional
  public ViewInquiryDto updateInquiryReply(UpdateInquiryReplyDto.Request request, User user) {
    InquiryReply nowReply = inquiryReplyRepository.findById(request.getInquiryReplyId())
        .orElseThrow(() -> new CustomException(INQUIRY_REPLY_NOT_FOUND));

    if (!nowReply.getAdmin().equals(user)) {
      throw new CustomException(NO_PERMISSION_TO_UPDATE);
    }

    nowReply.updateReply(request);

    Inquiry nowInquiry = inquiryRepository.findByInquiryReply(nowReply)
        .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

    return ViewInquiryDto.toDto(nowInquiry);
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
