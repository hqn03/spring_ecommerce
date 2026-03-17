package github.hqn03.auth_service.cart.repository;

import github.hqn03.auth_service.cart.entity.Cart;
import github.hqn03.auth_service.cart.entity.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndSkuId(Cart cart, Long skuId);

    @Override
    @EntityGraph(attributePaths = {
            "sku",
            "sku.product",
            "sku.color",
            "sku.size",
    })
    List<CartItem> findAllById(Iterable<Long> longs);
}
