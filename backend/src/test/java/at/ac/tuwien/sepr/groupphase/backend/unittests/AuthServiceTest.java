package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.LoginTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AuthRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.AuthenticationCache;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.AuthRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthServiceTest implements TestData, UserTestData, LoginTestData {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;

    private Supplier<AuthenticationCache> authRepositorySupplier = () -> {
        return new AuthenticationCache(TEST_NONCE, TEST_PUBKEY);
    };


    @Test
    public void generateNonceWorksAsIntendedForValidPubKey() {
        authService.generateNonce(TEST_PUBKEY);
        Optional<AuthenticationCache> result = authRepository.findAuthCacheByPublicKey(TEST_PUBKEY);
        assertTrue(result.isPresent());
    }

    @Test
    public void failedLoginThrowsBadCredentialsExceptionAndNonceGetsDeleted() {
        assertThrows(BadCredentialsException.class, () -> {

            authService.generateNonce(TEST_PUBKEY);
            Optional<AuthenticationCache> result = authRepository.findAuthCacheByPublicKey(TEST_PUBKEY);
            authService.login(new AuthRequestDto());
            assertFalse(result.isPresent());
        });

    }

    @Test
    public void validSignatureCreatesNewUserIfNotExists() {
        authRepository.save(authRepositorySupplier.get());
        AuthRequestDto testRequest = new AuthRequestDto();
        testRequest.setSignature(TEST_SIGNATURE);
        testRequest.setPublicKey(TEST_PUBKEY);
        authService.login(testRequest);

        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(result.isPresent());
    }

}
