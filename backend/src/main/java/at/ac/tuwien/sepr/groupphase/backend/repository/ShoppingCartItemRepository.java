package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ShoppingCartItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

    List<ShoppingCartItem> findByUserId(Long id);

    Optional<ShoppingCartItem> findByPlushToyAndUser(PlushToy plushToy, User user);

    List<ShoppingCartItem> findByUserPublicKey(String publicKey);
}
