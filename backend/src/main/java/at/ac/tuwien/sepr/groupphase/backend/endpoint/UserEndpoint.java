package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ShoppingCartService;
import at.ac.tuwien.sepr.groupphase.backend.service.SolanaService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SolanaService solanaService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public UserEndpoint(UserService userService, OrderMapper orderMapper, UserMapper userMapper,
            UserRepository userRepository, SolanaService solanaService, ShoppingCartService shoppingCartService) {
        this.userService = userService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.solanaService = solanaService;
        this.shoppingCartService = shoppingCartService;
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    @Operation(summary = "Delete a user", description = "Deletes a user from the system using their public key", security = @SecurityRequirement(name = "apiKey"))
    public void delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("DELETE /api/v1/user {}", publicKey);
        userService.deleteUser(publicKey);
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @GetMapping(value = "/orders")
    @Operation(summary = "Get the order history of the user", security = @SecurityRequirement(name = "apiKey"))
    public List<OrderListDto> getOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        LOGGER.info("GET /api/v1/user/orders {}", publicKey);
        List<OrderListDto> orders = userService.getOrderHistory(publicKey);
        return orders;
    }

    @RolesAllowed({ "USER", "ADMIN" })
    @GetMapping
    @Operation(summary = "Get user details", description = "Fetches the logged in user's details", security = @SecurityRequirement(name = "apiKey"))
    public UserDetailDto getUserByPublicKey() {
        LOGGER.info("GET /api/v1/user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();
        return userMapper.toDto(userService.findUserByPublicKey(publicKey));
    }

    @RolesAllowed({ "USER", "ADMIN" })
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

    @RolesAllowed({ "USER", "ADMIN" })
    @PostMapping(value = "/orders")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Verify and Create an Order", security = @SecurityRequirement(name = "apiKey"))
    public OrderDetailDto verifyAndCreateOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        LOGGER.info("POST /api/v1/user/orders {}", orderCreateDto);

        OrderDetailDto orderDetail = new OrderDetailDto();

        if (userService.isValidTransaction(orderCreateDto.getSignature())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String publicKey = authentication.getName();
            orderDetail = shoppingCartService.convertCartToOrder(publicKey);
            List<PlushToyCartListDto> cart = shoppingCartService.getFullCart(publicKey);
            for (PlushToyCartListDto item : cart) {
                for (int i = 0; i < item.getAmount(); i++) {
                    solanaService.mintNft(item.getId(), publicKey);
                }
            }
            OrderDetailDto placeholder = new OrderDetailDto();
            placeholder.setId(1L);
            placeholder.setTotalPrice(100.0);
            placeholder.setTotalTax(20.0);
            placeholder.setTimestamp(LocalDateTime.now());
            return orderDetail;
        }
        return orderDetail;
    }
}
