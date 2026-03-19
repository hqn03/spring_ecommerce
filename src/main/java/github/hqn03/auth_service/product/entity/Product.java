package github.hqn03.auth_service.product.entity;

import github.hqn03.auth_service.category.entity.Category;
import github.hqn03.auth_service.common.entity.BaseEntity;
import github.hqn03.auth_service.sku.entity.Sku;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SoftDelete;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@SoftDelete
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Sku> skus = new HashSet<>();

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new LinkedHashSet<>();

    public void addSku(Sku sku) {
        skus.add(sku);
        sku.setProduct(this);
    }
}
