package at.ac.tuwien.sepr.groupphase.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttribute;

@Repository
public interface PlushToyAttributeRepository extends JpaRepository<PlushToyAttribute, Long> {

    /**
     * Finds a plush toy attribute by their name.
     *
     * @param name the name of the plush toy attribute
     * @return an Optional containing the plush toy attribute if found, or an empty
     */
    Optional<PlushToyAttribute> findByName(String name);
}
