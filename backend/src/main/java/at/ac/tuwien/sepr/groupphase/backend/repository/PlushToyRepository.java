package at.ac.tuwien.sepr.groupphase.backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;

@Repository
public interface PlushToyRepository extends JpaRepository<PlushToy, Long> {

    @Query("SELECT p FROM PlushToy p WHERE "
        + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) OR "
        + "(LOWER(p.description) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<PlushToy> searchPlushToys(@Param("name") String name);

}
