package com.example.Ecommerce.inquiry.repository;

import com.example.Ecommerce.inquiry.domain.Inquiry;
import com.example.Ecommerce.inquiry.domain.InquiryReply;
import com.example.Ecommerce.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

  Page<Inquiry> findAllByUser(Pageable pageable, User user);
  Page<Inquiry> findAll(Pageable pageable);
  Page<Inquiry> findAllByReplyState(Pageable pageable, Boolean replyState);

  Optional<Inquiry> findByInquiryReply(InquiryReply inquiryReply);
}
