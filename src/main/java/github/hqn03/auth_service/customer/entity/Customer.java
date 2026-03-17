package github.hqn03.auth_service.customer.entity;

import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@Getter
@Setter
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    @Column(name = "total_spent")
    private BigDecimal totalSpent = BigDecimal.ZERO;
}
