package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserServiceTest implements UserTestData {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    public void givenValidPublicKey_whenDeleteUser_thenUserIsDeleted() {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        userRepository.save(user);

        userService.deleteUser(TEST_PUBKEY);
        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertFalse(result.isPresent());
    }

    @Test
    public void givenInvalidPublicKey_whenDeleteUser_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(TEST_NONEXISTENT_PUBKEY));
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
