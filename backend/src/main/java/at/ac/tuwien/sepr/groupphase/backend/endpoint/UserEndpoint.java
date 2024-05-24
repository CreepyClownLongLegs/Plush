package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @RolesAllowed({"USER", "ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    @Operation(summary = "Delete a user", description = "Deletes a user from the system using their public key", security = @SecurityRequirement(name = "apiKey"))
    public void delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("DELETE /api/v1/user {}", publicKey);
        userService.deleteUser(publicKey);
    }
}
