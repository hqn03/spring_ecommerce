package github.hqn03.auth_service.sku.entity;

import github.hqn03.auth_service.attribute.entity.Color;
import github.hqn03.auth_service.attribute.entity.Size;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.product.entity.Product;
import github.hqn03.auth_service.product.entity.ProductImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "skus")
@SoftDelete
@NoArgsConstructor
@Getter
@Setter
public class Sku extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @Column(unique = true, nullable = false)
    private String skuCode;

    private BigDecimal price = BigDecimal.ZERO;

    private Integer stockQty = 0;

    private Integer discountPercent  = 0;

    @OneToMany(mappedBy = "sku",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new LinkedHashSet<>();

    public BigDecimal getDiscountAmount() {
        if (discountPercent == 0) {
            return BigDecimal.ZERO;
        }

        return this.price.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }
}
