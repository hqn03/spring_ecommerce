package github.hqn03.auth_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@Getter
@Setter
public class ProductImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    private Sku sku;

    @Column(nullable = false)
    private String imageUrl;

    private Boolean isMain = Boolean.FALSE;

    private Integer sortOrder = 1;
}
