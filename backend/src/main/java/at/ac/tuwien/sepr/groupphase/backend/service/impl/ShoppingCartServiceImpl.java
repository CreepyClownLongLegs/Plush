package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderItemListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ShoppingCartItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.entity.OrderItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import at.ac.tuwien.sepr.groupphase.backend.entity.DeliveryStatus;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShoppingCartItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ShoppingCartService;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ShoppingCartServiceImpl(PlushToyRepository plushToyRepository,
            ShoppingCartItemRepository shoppingCartItemRepository, UserRepository userRepository) {
        this.plushToyRepository = plushToyRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addToCart(String publicKey, long itemId) {
        LOGGER.debug("Adding item to cart: itemId={}, publicKey={}", itemId, publicKey);

        User user = userRepository.findUserByPublicKey(publicKey)
                .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        PlushToy plushToy = plushToyRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Plush toy not found with id: " + itemId));

        Optional<ShoppingCartItem> itemOptional = shoppingCartItemRepository.findByPlushToyAndUser(plushToy, user);

        ShoppingCartItem cartItem;
        if (itemOptional.isPresent()) {
            cartItem = itemOptional.get();
            cartItem.setAmount(cartItem.getAmount() + 1);
        } else {
            cartItem = new ShoppingCartItem();
            cartItem.setUser(user);
            cartItem.setPlushToy(plushToy);
            cartItem.setAmount(1);
        }

        shoppingCartItemRepository.save(cartItem);
    }


    @Override
    public void deleteFromCart(String publicKey, long itemId) throws NotFoundException {
        LOGGER.debug("Deleting item from cart: {}", itemId);

        User user = userRepository.findUserByPublicKey(publicKey)
                .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        PlushToy plushToy = plushToyRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Plush toy not found with id: " + itemId));

        ShoppingCartItem cartItem = shoppingCartItemRepository.findByPlushToyAndUser(plushToy, user)
                .orElseThrow(() -> new NotFoundException(
                        "Item not found in cart, itemId: " + itemId + " publicKey: " + publicKey));

        shoppingCartItemRepository.delete(cartItem);
    }


    @Override
    public OrderListDto convertCartToOrder() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String publicKey = authentication.getName();

        LOGGER.debug("Creating an order from the shopping cart: {}", publicKey);

        User user = userRepository.findUserByPublicKey(publicKey)
            .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new NotFoundException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryStatus(new DeliveryStatus(2, "pending"));
        order.setTimestamp(LocalDateTime.now());

        List<OrderItemListDto> orderItemsDto = new ArrayList<>();

        double totalPrice = 0;
        double totalTax = 0;

        for (ShoppingCartItem cartItem : cartItems) {
            PlushToy plushToy = cartItem.getPlushToy();
            int amount = cartItem.getAmount();
            double pricePerPiece = plushToy.getPrice();
            double taxClass = 0.20;
            double taxAmount = pricePerPiece * taxClass * amount;

            totalPrice += pricePerPiece * amount;
            totalTax += taxAmount;

            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(amount);
            orderItem.setName(plushToy.getName());
            orderItem.setPricePerPiece(pricePerPiece);
            orderItem.setTaxAmount(taxAmount);
            orderItem.setTaxClass((float) taxClass);
            orderItem.setPlushToy(plushToy);
            orderItem.setImageUrl(plushToy.getImageUrl());
            orderItem.setOrder(order);

            order.addOrderItem(orderItem);

            OrderItemListDto orderItemDto = new OrderItemListDto();
            orderItemDto.setId(orderItem.getId());
            orderItemDto.setName(plushToy.getName());
            orderItemDto.setPricePerPiece(pricePerPiece);
            orderItemDto.setAmount(amount);
            orderItemDto.setImageUrl(plushToy.getImageUrl());
            orderItemDto.setPlushToyId(plushToy.getId());
            orderItemsDto.add(orderItemDto);
        }

        order.setTotalPrice(totalPrice);
        order.setTotalTax(totalTax);

        orderRepository.save(order);
        shoppingCartItemRepository.deleteAll(cartItems);

        OrderListDto orderListDto = new OrderListDto();
        orderListDto.setId(order.getId());
        orderListDto.setTotalPrice(totalPrice);
        orderListDto.setTimestamp(order.getTimestamp());
        orderListDto.setTotalTax(totalTax);
        orderListDto.setOrderItems(orderItemsDto);
        orderListDto.setDeliveryStatus(order.getDeliveryStatus().getStatus());

        return orderListDto;
    }





    @Override
    public void decreaseAmount(String publicKey, Long itemId) throws NotFoundException {
        LOGGER.debug("Decreasing amount of items for shopping cart item: {}", itemId);

        User user = userRepository.findUserByPublicKey(publicKey)
                .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        PlushToy plushToy = plushToyRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Plush toy not found with id: " + itemId));

        ShoppingCartItem cartItem = shoppingCartItemRepository.findByPlushToyAndUser(plushToy, user)
                .orElseThrow(() -> new NotFoundException(
                        "Item not found in cart, itemId: " + itemId + " publicKey: " + publicKey));

        if (cartItem.getAmount() > 1) {
            cartItem.setAmount(cartItem.getAmount() - 1);
            shoppingCartItemRepository.save(cartItem);
        } else if (cartItem.getAmount() == 1) {
            deleteFromCart(publicKey, itemId);
        }

    }

    @Override
    public List<PlushToyCartListDto> getFullCart(String publicKey) {
        LOGGER.debug("Fetching full cart for user with publicKey: {}", publicKey);

        User user = userRepository.findUserByPublicKey(publicKey)
                .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());

        return cartItems.stream()
                .map(cartItem -> {
                    PlushToy plushToy = cartItem.getPlushToy();
                    PlushToyCartListDto dto = new PlushToyCartListDto();
                    dto.setId(plushToy.getId());
                    dto.setName(plushToy.getName());
                    dto.setDescription(plushToy.getDescription());
                    dto.setPrice(plushToy.getPrice());
                    dto.setAverageRating(plushToy.getAverageRating());
                    dto.setHp(plushToy.getHp());
                    dto.setImageUrl(plushToy.getImageUrl());
                    dto.setAmount(cartItem.getAmount());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void clearCart(String publicKey) {
        LOGGER.debug("Clearing cart for user with publicKey: {}", publicKey);

        User user = userRepository.findUserByPublicKey(publicKey)
                .orElseThrow(() -> new NotFoundException("User not found with publicKey: " + publicKey));

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByUserId(user.getId());

        shoppingCartItemRepository.deleteAll(cartItems);
    }
}
