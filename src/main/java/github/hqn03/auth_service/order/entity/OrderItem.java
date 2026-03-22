package github.hqn03.auth_service.order.entity;

import github.hqn03.auth_service.sku.entity.Sku;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String skuCode;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String variant;

    private String image;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return Objects.equals(((OrderItem) obj).getSkuCode(), this.getSkuCode());
    }
}
