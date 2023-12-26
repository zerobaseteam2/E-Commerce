package com.example.Ecommerce.interesthistory.domain;

import com.example.Ecommerce.interesthistory.dto.InterestHistoryCreateDto;
import com.example.Ecommerce.product.domain.BaseEntity;
import com.example.Ecommerce.product.domain.Product;
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
@Table(name = "InterestHistory")
@DynamicUpdate
public class InterestHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer productSalePrice;

    public static InterestHistory of(InterestHistoryCreateDto.Request request, User user, Product product) {
        InterestHistory interestHistory = new InterestHistory();
        interestHistory.setUser(user);
        interestHistory.setProduct(product);
        interestHistory.setProductName(product.getName());
        interestHistory.setProductSalePrice(product.getPrice());
        return interestHistory;
    }
}