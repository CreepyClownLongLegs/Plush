package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShoppingCartItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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


    @BeforeEach
    public void setUp() {
        shoppingCartItemRepository.deleteAll();
        plushToyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void givenValidItemIdAndPublicKey_whenAddToCart_thenItemAddedSuccessfully() {
        String publicKey = "validPublicKey";

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        PlushToy plushy = new PlushToy();
        plushy.setName("Test");
        plushy.setPrice(10.0);
        plushy.setTaxClass(10.0f);
        plushy.setDescription("Feisty lil fella");
        plushy.setDescription("Cute lil gent");
        plushy.setDescription("this one has seen better days");
        plushy.setWeight(1);
        plushy.setColor(Color.BLACK);
        plushy.setSize(Size.MEDIUM);
        plushy.setHp(100);
        plushy.setStrength(5);
        plushToyRepository.save(plushy);

        shoppingCartService.addToCart(publicKey, plushToyRepository.findAll().getFirst().getId());

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());
        assertEquals(1, cartItems.size());
    }

    @Test
    public void givenNonExistingItem_whenAddToCart_thenNotFoundExceptionThrown() {
        String publicKey = "validPublicKey";
        long nonExistingItemId = 999L;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        assertThrows(NotFoundException.class, () -> shoppingCartService.addToCart(publicKey, nonExistingItemId));
    }

    @Test
    public void givenValidItemIdAndPublicKey_whenDeleteFromCart_thenItemRemovedSuccessfully() {
        String publicKey = "validPublicKey";

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        PlushToy plushy = new PlushToy();
        plushy.setName("Test");
        plushy.setPrice(10.0);
        plushy.setTaxClass(10.0f);
        plushy.setDescription("Feisty lil fella");
        plushy.setDescription("Cute lil gent");
        plushy.setDescription("this one has seen better days");
        plushy.setWeight(1);
        plushy.setColor(Color.BLACK);
        plushy.setSize(Size.MEDIUM);
        plushy.setHp(100);
        plushy.setStrength(5);

        plushToyRepository.save(plushy);

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setUser(user);
        cartItem.setPlushToy(plushy);
        cartItem.setAmount(1);
        shoppingCartItemRepository.save(cartItem);
        PlushToyCartListDto plushCartDto = shoppingCartService.getFullCart(publicKey).getFirst();
        shoppingCartService.deleteFromCart(publicKey, plushCartDto.getCartItemId());

        Optional<ShoppingCartItem> cartItems = shoppingCartItemRepository.findById(1L);
        assertTrue(cartItems.isEmpty());
    }

    @Test
    public void givenNonExistingItem_whenDeleteFromCart_thenNotFoundExceptionThrown() {
        String publicKey = "validPublicKey";
        long nonExistingItemId = 999L;

        User user = new User();
        user.setPublicKey(publicKey);
        userRepository.save(user);

        assertThrows(NotFoundException.class, () -> shoppingCartService.deleteFromCart(publicKey, nonExistingItemId));
    }


}
