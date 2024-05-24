package at.ac.tuwien.sepr.groupphase.backend.service;

import org.springframework.lang.NonNull;

import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;

public interface SolanaService {

    /**
     * Create a smart contract, mint for a new plush toy. The smart contract is
     * saved in the database.
     *
     * @param plushToyId the plush toy to create a smart contract for
     * @return a the created smart contract
     */
    SmartContract createSmartContract(@NonNull Long plushToyId);

}
