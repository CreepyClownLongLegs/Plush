package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import java.util.Optional;

import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements UserTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

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
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        userRepository.save(user);

        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(result.isPresent());
    }

    @Test
    public void givenInvalidPublicKey_whenFindUser_thenUserNotFound() {
        Optional<User> result = userRepository.findUserByPublicKey(TEST_NONEXISTENT_PUBKEY);
        assertFalse(result.isPresent());
    }
}
