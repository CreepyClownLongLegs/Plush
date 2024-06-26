package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/api/v1/cart")
public class ShoppingCartEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartEndpoint(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @PostMapping
    @Operation(summary = "Add an item to the shopping cart")
    public void addToCart(@RequestBody long plushToyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("POST /api/v1/cart/ - {}", plushToyId);
        shoppingCartService.addToCart(publicKey, plushToyId);
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @DeleteMapping
    @Operation(summary = "Delete an item from the shopping cart")
    public void deleteFromCart(@RequestParam("plushToyId") long plushToyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("DELETE /api/v1/cart/ - itemId: {}", plushToyId);
        shoppingCartService.deleteFromCart(publicKey, plushToyId);
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @GetMapping
    @Operation(summary = "Get the full cart for a user")
    public List<PlushToyCartListDto> getFullCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("GET /api/v1/cart/ - publicKey: {}", publicKey);
        return shoppingCartService.getFullCart(publicKey);
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @PostMapping("/decrease")
    @Operation(summary = "Decrease the amount of an item in the shopping cart")
    public void decreaseAmount(@RequestBody long plushToyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("POST /api/v1/cart/decrease - itemId: {}", plushToyId);
        shoppingCartService.decreaseAmount(publicKey, plushToyId);
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @DeleteMapping("/clear")
    @Operation(summary = "Clear the shopping cart")
    public void clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("DELETE /api/v1/cart/clear - publicKey: {}", publicKey);
        shoppingCartService.clearCart(publicKey);
    }

}
