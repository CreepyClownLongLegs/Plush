package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_COLOR;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_NAME;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_PRICE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_SIZE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_TAX_CLASS;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_WEIGHT;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData.TEST_PUBKEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.entity.ShoppingCartItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShoppingCartItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ShoppingCartServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ShoppingCartServiceTest implements TestData {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Supplier<PlushToy> plushySupplier = () -> {
        PlushToy plushy = new PlushToy();
        plushy.setName(TEST_PLUSHTOY_NAME);
        plushy.setPrice(TEST_PLUSHTOY_PRICE);
        plushy.setTaxClass(TEST_PLUSHTOY_TAX_CLASS);
        plushy.setWeight(TEST_PLUSHTOY_WEIGHT);
        plushy.setColor(Color.valueOf(TEST_PLUSHTOY_COLOR));
        plushy.setSize(Size.valueOf(TEST_PLUSHTOY_SIZE));
        return plushy;
    };

    @BeforeEach
    public void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        shoppingCartItemRepository.deleteAll();
        plushToyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void givenValidItemIdAndPublicKey_whenAddToCart_thenItemAddedSuccessfully() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        plushToyRepository.save(plushySupplier.get());

        shoppingCartService.addToCart(publicKey, plushToyRepository.findAll().getFirst().getId());

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());
        assertEquals(1, cartItems.size());
    }

    @Test
    public void givenNonExistingItem_whenAddToCart_thenNotFoundExceptionThrown() {
        String publicKey = TEST_PUBKEY;
        long nonExistingItemId = 999L;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        assertThrows(NotFoundException.class, () -> shoppingCartService.addToCart(publicKey, nonExistingItemId));
    }

    @Test
    public void givenValidItemIdAndPublicKey_whenDeleteFromCart_thenItemRemovedSuccessfully() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);
        PlushToy plushy = plushySupplier.get();
        plushToyRepository.save(plushy);

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setUser(user);
        cartItem.setPlushToy(plushy);
        cartItem.setAmount(1);
        shoppingCartItemRepository.save(cartItem);
        PlushToyCartListDto plushCartDto = shoppingCartService.getFullCart(publicKey).getFirst();
        shoppingCartService.deleteFromCart(publicKey, plushCartDto.getId());

        Optional<ShoppingCartItem> cartItems = shoppingCartItemRepository.findById(1L);
        assertTrue(cartItems.isEmpty());
    }

    @Test
    public void givenNonExistingItem_whenDeleteFromCart_thenNotFoundExceptionThrown() {
        String publicKey = TEST_PUBKEY;
        long nonExistingItemId = 999L;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        assertThrows(NotFoundException.class, () -> shoppingCartService.deleteFromCart(publicKey, nonExistingItemId));
    }

    @Test
    public void givenValidItemIdAndPublicKey_whenDecreaseAmount_thenAmountDecreasedSuccessfully() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        PlushToy plushToy = plushySupplier.get();
        plushToyRepository.save(plushToy).getId();

        ShoppingCartItem item = new ShoppingCartItem();
        item.setUser(user);
        item.setPlushToy(plushToy);
        item.setAmount(2); // Initial amount set to 2
        item = shoppingCartItemRepository.save(item);

        shoppingCartService.decreaseAmount(publicKey, plushToy.getId());

        ShoppingCartItem updatedItem = shoppingCartItemRepository.findById(item.getId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        assertEquals(1, updatedItem.getAmount()); // Check if the amount decreased by 1
    }

    @Test
    public void givenInvalidItemIdAndPublicKey_whenDecreaseAmount_thenNotFoundExceptionThrown() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        long nonExistingItemId = 999L;

        assertThrows(NotFoundException.class, () -> shoppingCartService.decreaseAmount(publicKey, nonExistingItemId));
    }

    @Test
    public void givenMultipleItemsInCart_whenClearCart_thenCartIsEmpty() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        PlushToy plushToy1 = plushToyRepository.save(plushySupplier.get());
        ShoppingCartItem item1 = new ShoppingCartItem();
        item1.setUser(user);
        item1.setPlushToy(plushToy1);
        item1.setAmount(1);
        shoppingCartItemRepository.save(item1);

        PlushToy plushToy2 = plushToyRepository.save(plushySupplier.get());
        ShoppingCartItem item2 = new ShoppingCartItem();
        item2.setUser(user);
        item2.setPlushToy(plushToy2);
        item2.setAmount(2);
        shoppingCartItemRepository.save(item2);

        PlushToy plushToy3 = plushToyRepository.save(plushySupplier.get());
        ShoppingCartItem item3 = new ShoppingCartItem();
        item3.setUser(user);
        item3.setPlushToy(plushToy3);
        item3.setAmount(3);
        shoppingCartItemRepository.save(item3);

        shoppingCartService.clearCart(publicKey);

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());
        assertTrue(cartItems.isEmpty());
    }

    @Test
    public void givenCartWithItems_whenConvertCartToOrder_thenOrderCreatedSuccessfully() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        PlushToy plushToy = plushySupplier.get();
        plushToyRepository.save(plushToy);

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setUser(user);
        cartItem.setPlushToy(plushToy);
        cartItem.setAmount(2);
        shoppingCartItemRepository.save(cartItem);

        OrderDetailDto orderDetailDto = shoppingCartService.convertCartToOrder(publicKey);

        assertEquals(1, orderDetailDto.getOrderItems().size());
        assertEquals(2, orderDetailDto.getOrderItems().get(0).getAmount());
        assertEquals(plushToy.getId(), orderDetailDto.getOrderItems().get(0).getPlushToyId());
        assertEquals(plushToy.getPrice() * 2, orderDetailDto.getTotalPrice());
        assertTrue(shoppingCartItemRepository.findByUserId(user.getId()).isEmpty());
    }


    @Test
    public void givenEmptyCart_whenConvertCartToOrder_thenNotFoundExceptionThrown() {
        String publicKey = TEST_PUBKEY;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        assertThrows(NotFoundException.class, () -> shoppingCartService.convertCartToOrder(publicKey));
    }



}
