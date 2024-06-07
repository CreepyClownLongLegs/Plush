package at.ac.tuwien.sepr.groupphase.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;

@Repository
public interface SmartContractRepository extends JpaRepository<SmartContract, Long> {
    /**
     * Finds a smart contract by their public key.
     *
     * @param publicKey the public key of the smart contract
     * @return an Optional containing the smart contract if found, or an empty
     *         Optional if not
     */
    Optional<SmartContract> findByPublicKey(String publicKey);

    /**
     * Finds a list of smart contracts by the plush toy id.
     *
     * @param plushToyId the plush toy id
     * @return a list of smart contracts
     */
    List<SmartContract> findByPlushToyId(Long plushToyId);
}
