package at.ac.tuwien.sepr.groupphase.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;

@Repository
public interface PlushToyRepository extends JpaRepository<PlushToy, Long> {
    
}
