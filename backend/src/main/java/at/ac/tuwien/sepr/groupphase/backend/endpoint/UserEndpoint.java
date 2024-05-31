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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;


import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, UserMapper userMapper) {

        this.userService = userService;
        this.userMapper = userMapper;
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

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping
    @Operation(summary = "Get user details", description = "Fetches the logged in user's details", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto getUserByPublicKey() {
        LOGGER.info("GET /api/v1/user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        return userMapper.toDto(userService.findUserByPublicKey(publicKey));
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PutMapping
    @Operation(summary = "Edit user details", description = "Updates the logged in user's details", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDetailDto userDetailDto) {
        try {
            LOGGER.info("PUT /api/v1/user");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String publicKey = authentication.getName();
            userService.updateUser(publicKey, userDetailDto);
            return ResponseEntity.ok(userDetailDto);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
