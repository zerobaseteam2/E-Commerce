package com.example.Ecommerce.inquiry.repository;

import com.example.Ecommerce.inquiry.domain.InquiryReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {
  boolean existsByInquiry_Id(Long inquiryId);

  void deleteByInquiry_Id(Long inquiryId);
}
