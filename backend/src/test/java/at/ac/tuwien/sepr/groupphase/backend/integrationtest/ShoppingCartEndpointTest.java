package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData.TEST_PUBKEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.common.PlushToySupplier;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ShoppingCartItemDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ShoppingCartItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShoppingCartItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ShoppingCartEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private PlushToySupplier plushToySupplier;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private PlushToy testPlushToy;
    private User testUser;

    @BeforeEach
    public void beforeEach() {
        shoppingCartItemRepository.deleteAll();
        userRepository.deleteAll();
        plushToyRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testUser = new User(TEST_PUBKEY);
        testUser = userRepository.save(testUser);

        testPlushToy = plushToyRepository.save(plushToySupplier.getPlushie());
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenValidItemId_whenDeleteFromCart_then200() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setUser(testUser);
        item.setPlushToy(testPlushToy);
        item.setAmount(1);
        shoppingCartItemRepository.save(item);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/cart")
                .param("itemId", String.valueOf(testPlushToy.getId())))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenValidPublicKey_whenGetFullCart_then200AndCartList() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setUser(testUser);
        item.setPlushToy(testPlushToy);
        item.setAmount(1);
        shoppingCartItemRepository.save(item);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ShoppingCartItemDto> cartList = objectMapper.readValue(jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ShoppingCartItemDto.class));

        assertTrue(cartList.size() > 0);
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenValidPublicKey_whenGetFullCart_then200AndEmptyCart() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ShoppingCartItemDto> cartList = objectMapper.readValue(jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ShoppingCartItemDto.class));

        assertTrue(cartList.isEmpty());
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenValidItemId_whenDecreaseAmount_then200() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setUser(testUser);
        item.setPlushToy(testPlushToy);
        item.setAmount(2); // Set amount to 2 for testing decrease
        shoppingCartItemRepository.save(item);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/cart/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(item.getId())))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        ShoppingCartItem updatedItem = shoppingCartItemRepository.findById(item.getId()).orElseThrow();
        assertEquals(1, updatedItem.getAmount()); // Check if the amount decreased by 1
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenInvalidItemId_whenDecreaseAmount_then404() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/cart/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(999)))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }
}
