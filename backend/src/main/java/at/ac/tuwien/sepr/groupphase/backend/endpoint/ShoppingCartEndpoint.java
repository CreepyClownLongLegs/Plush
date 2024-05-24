package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
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

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cart")
public class ShoppingCartEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartEndpoint(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PermitAll
    @PostMapping(value = "/add")
    @Operation(summary = "Add an item to the shopping cart")
    public void addToCart(@RequestBody long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("POST /api/v1/cart/add - {}", itemId);
        shoppingCartService.addToCart(publicKey, itemId);
    }

    @PermitAll
    @DeleteMapping(value = "/delete")
    @Operation(summary = "Delete an item from the shopping cart")
    public void deleteFromCart(@RequestParam("itemId") long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("DELETE /api/v1/cart/delete - itemId: {}", itemId);
        shoppingCartService.deleteFromCart(publicKey, itemId);
    }

    @PermitAll
    @GetMapping(value = "/full")
    @Operation(summary = "Get the full cart for a user")
    public List<PlushToyCartListDto> getFullCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("GET /api/v1/cart/full - publicKey: {}", publicKey);
        return shoppingCartService.getFullCart(publicKey);
    }

}
