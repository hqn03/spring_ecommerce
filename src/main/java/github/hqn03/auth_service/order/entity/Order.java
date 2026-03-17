package github.hqn03.auth_service.order.entity;

import github.hqn03.auth_service.common.constant.OrderStatus;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private LocalDateTime orderDate =  LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private String shippingFullName;

    @Column(nullable = false)
    private String shippingPhone;

    @Column(nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

    public void addItem (OrderItem item){
        this.items.add(item);
        item.setOrder(this);
    }
}
