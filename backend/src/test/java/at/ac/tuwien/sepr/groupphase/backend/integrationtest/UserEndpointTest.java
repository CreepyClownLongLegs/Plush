package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData.TEST_NONCE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData.TEST_SIGNATURE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.OrderTestData.ORDERS_BASE_URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AuthRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.AuthenticationCache;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.AuthRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.AuthServiceImplementation;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements UserTestData {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private Supplier<AuthenticationCache> authRepositorySupplier = () -> {
        return new AuthenticationCache(TEST_NONCE, TEST_PUBKEY);
    };
    private Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        return user;
    };
    @Autowired
    private AuthServiceImplementation authServiceImplementation;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenValidPublicKey_whenDeleteUser_then204() throws Exception {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        userRepository.save(user);

        MvcResult mvcResult = mockMvc.perform(delete(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());

        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertFalse(result.isPresent());
    }

    @Test
    @WithMockUser(username = TEST_NONEXISTENT_PUBKEY)
    public void givenInvalidPublicKey_whenDeleteUser_then404() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void givenValidPublicKey_whenFindUser_thenUserIsFound() {
        userRepository.save(userSupplier.get());


        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(result.isPresent());
    }

    @Test
    public void givenInvalidPublicKey_whenFindUser_thenUserNotFound() {
        Optional<User> result = userRepository.findUserByPublicKey(TEST_NONEXISTENT_PUBKEY);
        assertFalse(result.isPresent());
    }

    @Test
    public void givenNoAuthorizationToken_whenGetOrders_then403Forbidden() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(ORDERS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        assertEquals(HttpStatus.FORBIDDEN.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void givenValidAuthorizationToken_whenGetOrders_thenExpectedOrderListDto() throws Exception {

        userRepository.save(userSupplier.get());
        authRepository.save(authRepositorySupplier.get());

        AuthRequestDto testRequest = new AuthRequestDto();
        testRequest.setSignature(TEST_SIGNATURE);
        testRequest.setPublicKey(TEST_PUBKEY);
        String jwt = authServiceImplementation.login(testRequest);

        MvcResult mvcResult = mockMvc.perform(get(ORDERS_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON).header("Authorization",
                    "Bearer " + jwt))
            .andDo(print())
            .andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = TEST_PUBKEY)
    public void givenValidUserDetails_whenUpdateUser_thenUserIsUpdated() throws Exception {

        // Create and save a user to be updated
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        user.setFirstname("OldName");
        userRepository.save(user);

        // Create JSON string for the update request
        String requestBody = """
            {
                "publicKey": "%s",
                "firstname": "%s",
                "lastname": "%s",
                "emailAddress": "%s",
                "phoneNumber": "%s",
                "country": "%s",
                "city": "%s",
                "postalCode": "%s",
                "addressLine1": "%s",
                "addressLine2": "%s",
                "locked": %b,
                "admin": %b
            }
            """;
        requestBody = String.format(requestBody, TEST_PUBKEY, "NewName", "NewLastname", "newemail@example.com",
            "1234567890", "NewCountry", "NewCity", "12345", "New Address Line 1", "New Address Line 2",
            false, false);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        UserDetailDto userDetailsDto = new ObjectMapper().readValue(response.getContentAsString(), UserDetailDto.class);
        assertThat(userDetailsDto)
            .isNotNull()
            .hasFieldOrPropertyWithValue("publicKey", TEST_PUBKEY)
            .hasFieldOrPropertyWithValue("firstname", "NewName")
            .hasFieldOrPropertyWithValue("lastname", "NewLastname")
            .hasFieldOrPropertyWithValue("emailAddress", "newemail@example.com")
            .hasFieldOrPropertyWithValue("phoneNumber", "1234567890")
            .hasFieldOrPropertyWithValue("country", "NewCountry")
            .hasFieldOrPropertyWithValue("city", "NewCity")
            .hasFieldOrPropertyWithValue("postalCode", "12345")
            .hasFieldOrPropertyWithValue("addressLine1", "New Address Line 1")
            .hasFieldOrPropertyWithValue("addressLine2", "New Address Line 2")
            .hasFieldOrPropertyWithValue("locked", false)
            .hasFieldOrPropertyWithValue("admin", false);

        // Verify that the user details are updated in the database
        Optional<User> updatedUserOpt = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(updatedUserOpt.isPresent());
        User updatedUserInDb = updatedUserOpt.get();
        assertEquals("NewName", updatedUserInDb.getFirstname());
        assertEquals("NewLastname", updatedUserInDb.getLastname());
        assertEquals("newemail@example.com", updatedUserInDb.getEmailAddress());
        assertEquals("1234567890", updatedUserInDb.getPhoneNumber());
        assertEquals("NewCountry", updatedUserInDb.getCountry());
        assertEquals("NewCity", updatedUserInDb.getCity());
        assertEquals("12345", updatedUserInDb.getPostalCode());
        assertEquals("New Address Line 1", updatedUserInDb.getAddressLine1());
        assertEquals("New Address Line 2", updatedUserInDb.getAddressLine2());
        assertFalse(updatedUserInDb.isLocked());
        assertFalse(updatedUserInDb.isAdmin());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void givenInvalidUserDetailDto_whenUpdateUser_thenBadRequest() throws Exception {
        // Create invalid UserDetailDto
        String invalidUserDetailDto = "{\n" +
            "    \"firstname\": \"\",\n" + // Invalid: Empty firstname
            "    \"lastname\": \"Doe\",\n" +
            "    \"emailAddress\": \"john.doe@example.com\",\n" +
            "    \"phoneNumber\": \"1234567890\",\n" +
            "    \"locked\": false,\n" +
            "    \"country\": \"USA\",\n" +
            "    \"postalCode\": \"12345\",\n" +
            "    \"city\": \"New York\",\n" +
            "    \"addressLine1\": \"123 Main St\",\n" +
            "    \"addressLine2\": \"Apt 101\",\n" +
            "    \"isAdmin\": false\n" +
            "}";

        // Perform PUT request with invalid UserDetailDto
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserDetailDto))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
