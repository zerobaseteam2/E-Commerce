package com.example.Ecommerce.review.domain;

import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.product.domain.BaseEntity;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.review.dto.ReviewUpdateDto;
import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "review")
@DynamicUpdate
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderProduct_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private OrderProduct orderProduct;

    @Column(nullable = false)
    private String username;
    
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "stars")
    private Double stars;

    @Column(name = "replyState")
    private Boolean replyState;

    @Column(name = "reply")
    private String reply;
    
    public void update(ReviewUpdateDto.Request request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.stars = request.getStars();
    }
    
    public void addReply(String reply) {
        this.replyState = true;
        this.reply = reply;
    }

}