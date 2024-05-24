package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import org.springframework.lang.NonNull;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void deleteUser(@NonNull String publicKey) throws NotFoundException {
        LOGGER.info("deleteUser {}", publicKey);
        var user = userRepository.findUserByPublicKey(publicKey);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteByPublicKey(publicKey);
    }
}
