package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderItemListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import at.ac.tuwien.sepr.groupphase.backend.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyCartListDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShoppingCartItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ShoppingCartServiceImpl(PlushToyRepository plushToyRepository,
                                   ShoppingCartItemRepository shoppingCartItemRepository, UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository1, OrderItemRepository orderItemRepository1) {
        this.plushToyRepository = plushToyRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository1;
        this.orderItemRepository = orderItemRepository1;
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
    @Transactional
    public OrderDetailDto convertCartToOrder(String publicKey) throws NotFoundException {
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
        int i = 1;

        for (ShoppingCartItem cartItem : cartItems) {
            i++;
            PlushToy plushToy = cartItem.getPlushToy();
            int amount = cartItem.getAmount();
            double pricePerPiece = plushToy.getPrice();
            double taxClass = plushToy.getTaxClass();
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
            orderItem.setPosition(i);

            orderItem.setOrder(order);
            order.addOrderItem(orderItem);
            orderItemRepository.save(orderItem);

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

        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(order.getId());
        orderDetailDto.setTotalPrice(totalPrice);
        orderDetailDto.setTimestamp(order.getTimestamp());
        orderDetailDto.setTotalTax(totalTax);
        orderDetailDto.setOrderItems(orderItemsDto);

        return orderDetailDto;
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
