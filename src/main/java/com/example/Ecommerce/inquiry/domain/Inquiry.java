package com.example.Ecommerce.inquiry.domain;

import com.example.Ecommerce.inquiry.dto.UpdaterInquiryDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
public class Inquiry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private boolean replyState; //답변 상태

  @OneToOne(mappedBy = "inquiry")
  private InquiryReply inquiryReply;

  @CreatedDate
  @Column(nullable = false)
  private LocalDate createdAt;

  @Column
  @LastModifiedDate
  private LocalDate updatedAt;

  public void update(UpdaterInquiryDto.Request request) {
    this.title = request.getTitle();
    this.content = request.getContent();
  }

  public void addReply(InquiryReply inquiryReply) {
    this.inquiryReply = inquiryReply;
  }
}
