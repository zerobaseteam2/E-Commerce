package com.example.Ecommerce.inquiry.domain;

import com.example.Ecommerce.inquiry.dto.admin.UpdateInquiryReplyDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class InquiryReply {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "inquiry_id")
  private Inquiry inquiry;

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private User admin;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @CreatedDate
  @Column(nullable = false)
  private LocalDate createdAt;

  @Column
  @LastModifiedDate
  private LocalDate updatedAt;

  public void updateReply(UpdateInquiryReplyDto.Request request) {
    this.title = request.getTitle();
    this.content = request.getContent();
  }
}
