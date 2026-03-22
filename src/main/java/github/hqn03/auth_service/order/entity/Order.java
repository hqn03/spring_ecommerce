package github.hqn03.auth_service.order.entity;

import github.hqn03.auth_service.common.constant.OrderStatus;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String orderCode;

    @Builder.Default
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

    @PrePersist
    public void prePersist(){
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        String randomPart = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        this.orderCode = "ORD-" + datePart + '-' + randomPart;
    }
}
