package github.hqn03.auth_service.payment.entity;

import com.stripe.param.radar.PaymentEvaluationCreateParams;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.order.entity.Order;
import github.hqn03.auth_service.payment.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sessionId;

    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String paymentMethod;
}
