package github.hqn03.auth_service.inventory.entity;

import github.hqn03.auth_service.common.constant.InventoryHistoryType;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InventoryHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sku_id")
    private Sku sku;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer changeQty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryHistoryType type;

    private String note;

    @Column(nullable = false)
    private String referenceId;
}
