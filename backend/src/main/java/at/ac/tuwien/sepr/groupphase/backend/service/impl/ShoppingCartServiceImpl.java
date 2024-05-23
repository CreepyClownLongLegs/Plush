package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ShoppingCartItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShoppingCartItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserRepository userRepository;

    public ShoppingCartServiceImpl(PlushToyRepository plushToyRepository, ShoppingCartItemRepository shoppingCartItemRepository, UserRepository userRepository) {
        this.plushToyRepository = plushToyRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addToCart(String publicKey, long itemId) {
        LOGGER.debug("Adding item to cart: itemId={}, publicKey={}", itemId, publicKey);

        User user = userRepository.findUserByPublicKey(publicKey)
            .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        PlushToy plushToy = plushToyRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Plush toy not found with id: " + itemId));

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setUser(user);
        cartItem.setPlushToy(plushToy);
        cartItem.setAmount(1);

        shoppingCartItemRepository.save(cartItem);
    }


    @Override
    public void deleteFromCart(String publicKey, long itemId) throws NotFoundException {
        LOGGER.debug("Deleting item from cart: {}", itemId);


        ShoppingCartItem cartItem = shoppingCartItemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item not found in cart with id: " + itemId));

        shoppingCartItemRepository.delete(cartItem);
    }

    @Override
    public List<PlushToyCartListDto> getFullCart(String publicKey) {
        LOGGER.debug("Fetching full cart for user with publicKey: {}", publicKey);

        User user = userRepository.findUserByPublicKey(publicKey)
            .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());

        return cartItems.stream()
            .map(cartItem -> {
                PlushToy plushToy = cartItem.getPlushToy();
                PlushToyCartListDto dto = new PlushToyCartListDto();
                dto.setId(plushToy.getId());
                dto.setName(plushToy.getName());
                dto.setDescription(plushToy.getDescription());
                dto.setPrice(plushToy.getPrice());
                dto.setAverageRating(plushToy.getAverageRating());
                dto.setHp(plushToy.getHp());
                dto.setImageUrl(plushToy.getImageUrl());
                dto.setCartItemId(cartItem.getId());
                return dto;
            })
            .collect(Collectors.toList());
    }
}
