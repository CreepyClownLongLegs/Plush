package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.entity.AuthenticationCache;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.AuthRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.function.Supplier;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData.AUTH_BASE_URI;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData.TEST_NONCE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData.TEST_PUBKEY;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData.TEST_SIGNATURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    private Supplier<AuthenticationCache> authRepositorySupplier = () -> {
        return new AuthenticationCache(TEST_NONCE, TEST_PUBKEY);
    };

    @BeforeEach
    public void beforeEach() {
        authRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void givenInvalidSignature_whenLogin_thenForbidden() throws Exception {
        String requestBody = "{\"publicKey\": \"7mNTHP45KmDyNp8cF8eDct86M5TqdWGoCmy5Ac3wGqHH\", \"signature\": \"5E1n1WFLCZKPdSPzegwePyQjfJBBTZSfZ8C93HDzxsEFtKEhCtcnW1Zr5NofWK6rRtAXygfdKdhCY4NVysYh5Yte\"}";

        MvcResult mvcResult = mockMvc.perform(post(AUTH_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void givenValidSignature_whenLogin_then200() throws Exception {
        String requestBody = "{\"publicKey\": \"" + TEST_PUBKEY + "\", \"signature\": \"" + TEST_SIGNATURE + "\"}";
        authRepository.save(authRepositorySupplier.get());

        MvcResult mvcResult = mockMvc.perform(post(AUTH_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void givenMissingPublicKey_whenLogin_thenBadRequest() throws Exception {
        String requestBody = "{\"signature\": " + TEST_SIGNATURE + "}";

        MvcResult mvcResult = mockMvc.perform(post(AUTH_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
