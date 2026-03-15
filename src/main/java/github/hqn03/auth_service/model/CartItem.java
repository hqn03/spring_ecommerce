package github.hqn03.auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name ="cart_items")
@NoArgsConstructor
@Setter
@Getter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @Column(nullable = false)
    private Integer quantity = 1;

    public BigDecimal getSubTotal() {
        if(sku == null || sku.getPrice() == null || this.quantity == null)
            return BigDecimal.ZERO;

        return this.sku.getPrice().multiply(new BigDecimal(this.quantity)) ;
    }
}
