package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;

/**
 * Generates the admin users if no user with the public key exists.
 */
@Component
@Profile({"generateData", "dev-clean"})
public class TestAdminDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    // map of admins with name and public key
    private static final Map<String, String> ADMINS = Map.of("test-admin",
            "EFWHJmGb4Ybgo7cueShmv42mDKZTkCrFNTeTPikhxitM");
    private final UserRepository userRepository;

    public TestAdminDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void generate() {
        for (String name : ADMINS.keySet()) {
            String publicKey = ADMINS.get(name);
            userRepository.findUserByPublicKey(publicKey)
                    .or(() -> {
                        User admin = new User(true);
                        admin.setPublicKey(publicKey);
                        LOGGER.info("creating admin {} with public key {}", name, publicKey);
                        return Optional.of(userRepository.save(admin));
                    });
        }
    }

}
