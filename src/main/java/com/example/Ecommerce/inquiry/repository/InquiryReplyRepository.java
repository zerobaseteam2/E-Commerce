package com.example.Ecommerce.inquiry.repository;

import com.example.Ecommerce.inquiry.domain.InquiryReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {
  Optional<InquiryReply> findByInquiry_Id(Long inquiryId);
  void deleteByInquiry_Id(Long inquiryId);
}
