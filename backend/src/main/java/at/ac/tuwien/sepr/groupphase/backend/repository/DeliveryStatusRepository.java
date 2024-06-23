package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {

    /**
     * Retrieves a {@code DeliveryStatus} entity by its code.
     *
     * @param code the code of the {@code DeliveryStatus} entity to retrieve
     * @return an {@code Optional<DeliveryStatus>} containing the found {@code DeliveryStatus} entity
     */
    Optional<DeliveryStatus> findByCode(int code);
}

