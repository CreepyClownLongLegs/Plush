package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders of a user by their public key.
     *
     * @param publicKey the public key of the user
     * @return an Optional containing the List of Orders if found, or an empty Optional if not
     */
    Optional<List<Order>> findOrdersByUser_PublicKey(String publicKey);

    Optional<Order> findOrderById(Long id);
}

