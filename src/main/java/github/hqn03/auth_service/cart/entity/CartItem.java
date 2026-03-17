package github.hqn03.auth_service.cart.entity;

import github.hqn03.auth_service.sku.entity.Sku;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name ="cart_items")
@NoArgsConstructor
@Setter
@Getter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @Column(nullable = false)
    private Integer quantity = 0;

    public BigDecimal getPrice() {
        if (sku == null || sku.getPrice() == null) return BigDecimal.ZERO;

        BigDecimal originalPrice = sku.getPrice();

        return originalPrice.subtract(sku.getDiscountAmount());
    }

    public BigDecimal getSubTotal(){
        if (sku == null || sku.getPrice() == null) return BigDecimal.ZERO;

        return this.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CartItem that = (CartItem) obj;

        // So sánh dựa trên ID của Sku
        Long thisSkuId = (sku != null) ? sku.getId() : null;
        Long thatSkuId = (that.getSku() != null) ? that.getSku().getId() : null;

        return Objects.equals(thisSkuId, thatSkuId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
