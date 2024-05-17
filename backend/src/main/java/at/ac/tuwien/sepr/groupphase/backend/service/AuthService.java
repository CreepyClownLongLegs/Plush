package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.JwtDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NonceDto;
import org.springframework.security.authentication.BadCredentialsException;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AuthRequestDto;

public interface AuthService {
    /**
     * Authenticates a user using the provided credentials and generates a JWT token.
     *
     * @param userLoginDto The data transfer object containing the user's login credentials.
     * @return JwtDto The data transfer object containing the generated JWT token.
     * @throws BadCredentialsException If the provided credentials are invalid.
     */
    JwtDto login(AuthRequestDto userLoginDto) throws BadCredentialsException;

    /**
     * Generates a nonce for a given public key. The nonce is a random number used once in a cryptographic communication.
     *
     * @param publicKey The public key for which to generate a nonce.
     * @return NonceDto The data transfer object containing the generated nonce.
     */
    NonceDto generateNonce(String publicKey);
}
