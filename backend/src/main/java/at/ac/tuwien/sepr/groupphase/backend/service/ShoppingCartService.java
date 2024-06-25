package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderDetailDto;
import java.util.List;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

/**
 * Service interface for managing shopping cart operations.
 */
public interface ShoppingCartService {

    /**
     * Adds an item to the shopping cart.
     *
     * @param publicKey the public key of the user
     * @param plushToyId    the ID of the item to be added
     */
    void addToCart(String publicKey, long plushToyId);

    /**
     * Deletes an item from the shopping cart.
     *
     * @param publicKey the public key of the user
     * @param plushToyId    the ID of the plushToy to be deleted
     * @throws NotFoundException if the item is not found in the shopping cart
     */
    void deleteFromCart(String publicKey, long plushToyId) throws NotFoundException;

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
     * @param plushToyId    the ID of the plushToy to decrease the amount of
     * @throws NotFoundException if the item is not found in the shopping cart
     */
    void decreaseAmount(String publicKey, Long plushToyId) throws NotFoundException;

    /**
     * Clears the shopping cart for a user.
     *
     * @param publicKey the public key of the user
     */
    void clearCart(String publicKey);

    /**
     * Converts the items in the shopping cart into an order.
     *
     * @param publicKey The public key of the user whose shopping cart is being converted into an order.
     * @return OrderDetailDto representing the details of the created order
     * @throws NotFoundException if the user or items are not found
     */
    OrderDetailDto convertCartToOrder(String publicKey) throws NotFoundException;

}
