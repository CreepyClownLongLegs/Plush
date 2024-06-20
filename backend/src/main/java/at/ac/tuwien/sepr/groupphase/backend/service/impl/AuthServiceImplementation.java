package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sol4k.Base58;
import org.sol4k.PublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AuthRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NonceDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.AuthenticationCache;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.AuthRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.AuthService;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImplementation implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public AuthServiceImplementation(UserRepository userRepository, AuthRepository authRepository,
                                     JwtTokenizer jwtTokenizer) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Transactional
    @Override
    public NonceDto generateNonce(String publicKey) {
        LOGGER.info("generateNonce {}", publicKey);
        String nonce = UUID.randomUUID().toString();
        authRepository.deleteByPublicKey(publicKey);
        authRepository.save(new AuthenticationCache(nonce, publicKey));
        return new NonceDto(nonce);
    }

    @Scheduled(fixedRate = 60000)
    public void cleanupNonces() {
        Instant cutoff = Instant.now().minusSeconds(60);
        authRepository.deleteByTimestampBefore(cutoff);
    }

    @Override
    public String login(AuthRequestDto authRequestDto) throws BadCredentialsException {
        LOGGER.info("login {}", authRequestDto);
        String publicKey = authRequestDto.getPublicKey();
        AuthenticationCache authCache = authRepository.findAuthCacheByPublicKey(publicKey)
            .orElseThrow(() -> new BadCredentialsException("No valid nonce found"));
        authRepository.deleteById(authCache.getId());

        if (isValidSignature(publicKey, authCache.getNonce(), authRequestDto.getSignature())) {
            User user = userRepository.findUserByPublicKey(publicKey).orElseGet(() -> createUser(publicKey));

            if (!user.isLocked()) {
                List<String> roles = user.isAdmin() ? List.of("ROLE_ADMIN", "ROLE_USER") : List.of("ROLE_USER");
                return jwtTokenizer.getAuthToken(publicKey, roles);
            } else {
                throw new BadCredentialsException("Account is locked");
            }
        }
        throw new BadCredentialsException("Signature is invalid");
    }

    private User createUser(String publicKey) {
        LOGGER.info("createUser {}", publicKey);
        return userRepository.save(new User(publicKey));
    }

    private boolean isValidSignature(String walletAddress, String nonce, String signature) {
        LOGGER.info("isValidSignature {}{}{}", walletAddress, nonce, signature);

        String message = "To verify ownership, please sign this nonce: " + nonce;
        byte[] messageBytes = message.getBytes();
        PublicKey publicKey = new PublicKey(walletAddress);
        byte[] signatureBytes = Base58.decode(signature);
        return publicKey.verify(signatureBytes, messageBytes);
    }


}
