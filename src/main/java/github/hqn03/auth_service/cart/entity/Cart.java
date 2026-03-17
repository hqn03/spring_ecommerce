package github.hqn03.auth_service.cart.entity;

import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@Getter
@Setter
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @Column(unique = true)
    private  String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
    }

    public Cart(Customer customer, String sessionId) {
        this.customer = customer;
        this.sessionId = sessionId;
    }

    public BigDecimal getTotalPrice() {
        if (this.items == null || this.items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return this.items.stream().map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getTotalItems(){
        if (this.items == null || this.items.isEmpty()) {
            return 0;
        }

        return this.items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
