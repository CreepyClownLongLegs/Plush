package at.ac.tuwien.sepr.groupphase.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttributeDistribution;

@Repository
public interface PlushToyAttributeDistributionRepository extends JpaRepository<PlushToyAttributeDistribution, Long> {

}
