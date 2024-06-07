package at.ac.tuwien.sepr.groupphase.backend.service;

import org.springframework.lang.NonNull;

import at.ac.tuwien.sepr.groupphase.backend.entity.Nft;
import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

public interface SolanaService {

    /**
     * Create a smart contract, mint for a new plush toy. The smart contract is
     * saved in the database.
     *
     * @param plushToyId the plush toy to create a smart contract for
     * @return a the created smart contract
     * @throws NotFoundException if the plush toy does not exist
     */
    SmartContract createSmartContract(@NonNull Long plushToyId) throws NotFoundException;

    /**
     * Mint a NFT for a plush toy. The nft is
     * saved in the database.
     *
     * @param plushToyId        the plush toy to mint a NFT for
     * @param receiverPublicKey the public key of the receiver as Base58 token
     * @return the minted NFT
     * @throws NotFoundException if the plush toy does not exist or the smart
     *                           contract does not exist
     */
    Nft mintNft(@NonNull Long plushToyId, @NonNull String receiverPublicKey) throws NotFoundException;

}
