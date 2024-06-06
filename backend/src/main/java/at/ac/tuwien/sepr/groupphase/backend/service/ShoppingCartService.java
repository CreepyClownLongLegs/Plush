package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.List;

/**
 * Service interface for managing shopping cart operations.
 */
public interface ShoppingCartService {

    /**
     * Adds an item to the shopping cart.
     *
     * @param publicKey the public key of the user
     * @param itemId    the ID of the item to be added
     */
    void addToCart(String publicKey, long itemId);


    /**
     * Deletes an item from the shopping cart.
     *
     * @param publicKey the public key of the user
     * @param itemId    the ID of the item to be deleted
     * @throws NotFoundException if the item is not found in the shopping cart
     */
    void deleteFromCart(String publicKey, long itemId) throws NotFoundException;

    /**
     * Retrieves the full cart for a user.
     *
     * @param publicKey the public key of the user
     * @return a list of PlushToyListDto representing the items in the user's cart
     */
    List<PlushToyCartListDto> getFullCart(String publicKey);

    /**
     * Decreases the amount of an item in the shopping cart.
     *
     * @param publicKey the public key of the user
     * @param itemId    the ID of the item to decrease the amount of
     * @throws NotFoundException if the item is not found in the shopping cart
     */
    void decreaseAmount(String publicKey, long itemId) throws NotFoundException;
}
