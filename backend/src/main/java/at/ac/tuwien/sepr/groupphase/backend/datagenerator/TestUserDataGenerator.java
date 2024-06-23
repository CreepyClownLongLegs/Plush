package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

/**
 * Generates test users if no user with the public key exists.
 */
@Component
@Profile({"generateData", "dev-clean"})
public class TestUserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Map<String, String> TEST_USERS = Map.of(
        "Test Me1", "1AGhwXRZ4bkxpiBS3HJkvLSYNbrmUkDYapuzMyFQBUX1",
        "Test Me2", "1BZLg3Eo8hTu6NGWkvVMxSznjfwC2z1jGw34knPep6G2",
        "Test Me3", "1CYoeBW7dwvWr8sxU1kPfwaw5vv7rVGRbtXjkhxLDh5v"
    );
    private final UserRepository userRepository;

    public TestUserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void generate() {
        for (Map.Entry<String, String> entry : TEST_USERS.entrySet()) {
            String name = entry.getKey();
            String publicKey = entry.getValue();
            userRepository.findUserByPublicKey(publicKey)
                .or(() -> {
                    User testUser = new User(false); // Assuming false means regular user
                    testUser.setPublicKey(publicKey);
                    String[] nameParts = name.split(" ");
                    testUser.setFirstname(nameParts[0]);
                    testUser.setLastname(nameParts[1]);
                    LOGGER.info("Creating test user {} with public key {}", name, publicKey);
                    return Optional.of(userRepository.save(testUser));
                });
        }
    }
}
