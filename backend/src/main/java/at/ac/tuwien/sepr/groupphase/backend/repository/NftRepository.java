package at.ac.tuwien.sepr.groupphase.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nft;
import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;

@Repository
public interface NftRepository extends JpaRepository<Nft, Long> {
    /**
     * Finds a nft by their public key.
     *
     * @param publicKey the public key of the nft
     * @return an Optional containing the nft if found, or an empty
     *         Optional if not
     */
    Optional<SmartContract> findByPublicKey(String publicKey);
}
