package com.example.Ecommerce.interesthistory.domain;

import com.example.Ecommerce.product.domain.BaseEntity;
import com.example.Ecommerce.product.domain.Product;
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
public class InterestHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer productSalePrice;

    public static InterestHistory of(User user, Product product) {
        InterestHistory interestHistory = new InterestHistory();
        interestHistory.setUser(user);
        interestHistory.setProduct(product);
        interestHistory.setProductName(product.getName());
        interestHistory.setProductSalePrice(product.getPrice());
        return interestHistory;
    }
}