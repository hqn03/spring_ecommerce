package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Cart;
import github.hqn03.auth_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndSkuId(Cart cart, Long skuId);
}
