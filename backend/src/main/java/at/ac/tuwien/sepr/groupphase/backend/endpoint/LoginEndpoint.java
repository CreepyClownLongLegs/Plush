package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AuthRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NonceDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.NonceRequestDto;
import at.ac.tuwien.sepr.groupphase.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping(value = "/api/v1/authentication")
public class LoginEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AuthService authService;

    @Autowired
    public LoginEndpoint(AuthService authService) {
        this.authService = authService;
    }

    @PermitAll
    @PostMapping("/nonce")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Generate a nonce for a given public key", description = "This operation generates a nonce for the provided public key. The nonce is used in the authentication process.")
    public NonceDto generateNonce(@RequestBody NonceRequestDto nonceRequestDto) {
        LOGGER.info("POST /api/v1/nonce body: {}", nonceRequestDto);
        return authService.generateNonce(nonceRequestDto.getPublicKey());
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate a user and return a JWT", description = "This operation authenticates a user using their public key and signature, and returns a JWT for subsequent authenticated requests.")
    public String login(@RequestBody AuthRequestDto authRequestDto) {
        LOGGER.info("POST /api/v1/authentication body: {}", authRequestDto);
        return authService.login(authRequestDto);
    }
}
