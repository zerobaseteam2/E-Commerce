package com.example.Ecommerce.product.domain;

import com.example.Ecommerce.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_Id", nullable = false)
    private Product productId;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "orderDetailId", nullable = false)
//    private OrderDetail orderDetailId;

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


}