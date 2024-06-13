package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderItemListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import at.ac.tuwien.sepr.groupphase.backend.entity.OrderItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.DeliveryStatusRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.OrderTestData.TEST_ORDER_AMOUNT;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.OrderTestData.TEST_ORDER_DELIVERY_STATUS;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.OrderTestData.TEST_ORDER_DELIVERY_STATUS_STRING;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.OrderTestData.TEST_ORDER_POSITION;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.OrderTestData.TEST_ORDER_TIMESTAMP;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_COLOR;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_HP;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_NAME;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_PRICE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_SIZE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_STRENGTH;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_TAX_AMOUNT;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_TAX_CLASS;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_WEIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserServiceTest implements UserTestData {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeliveryStatusRepository deliveryStatusRepository;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void beforeEach() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        deliveryStatusRepository.deleteAll();

        userRepository.deleteAll();
    }

    private Supplier<PlushToy> plushySupplier = () -> {
        PlushToy plushy = new PlushToy();
        plushy.setName(TEST_PLUSHTOY_NAME);
        plushy.setPrice(TEST_PLUSHTOY_PRICE);
        plushy.setTaxClass(TEST_PLUSHTOY_TAX_CLASS);
        plushy.setWeight(TEST_PLUSHTOY_WEIGHT);
        plushy.setColor(Color.valueOf(TEST_PLUSHTOY_COLOR));
        plushy.setSize(Size.valueOf(TEST_PLUSHTOY_SIZE));
        plushy.setHp(TEST_PLUSHTOY_HP);
        plushy.setStrength(TEST_PLUSHTOY_STRENGTH);
        return plushy;
    };

    private Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        return user;
    };
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Test
    public void givenValidPublicKey_whenDeleteUser_thenUserIsDeleted() {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        userRepository.save(user);

        userService.deleteUser(TEST_PUBKEY);
        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertFalse(result.isPresent());
    }

    @Test
    public void givenInvalidPublicKey_whenDeleteUser_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(TEST_NONEXISTENT_PUBKEY));
    }

    @Test
    public void givenValidPublicKey_whenFindUser_thenUserIsFound() {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        userRepository.save(user);

        Optional<User> result = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(result.isPresent());
    }

    @Test
    public void givenInvalidPublicKey_whenFindUser_thenUserNotFound() {
        Optional<User> result = userRepository.findUserByPublicKey(TEST_NONEXISTENT_PUBKEY);
        assertFalse(result.isPresent());
    }

    @Test
    public void updateUserThrowsNotFoundExceptionWhenUserNotFound() {

        String publicKey = "nonexistent-public-key";
        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setPublicKey(publicKey);

        assertThrows(NotFoundException.class, () -> userService.updateUser(publicKey, userDetailDto));
    }

    @Test
    public void givenValidPublicKey_whenGetOrderHistory_thenReturnExpectedData() {
        userRepository.save(userSupplier.get());
        PlushToy plushToy = plushToyRepository.save(plushySupplier.get());
        deliveryStatusRepository.save(TEST_ORDER_DELIVERY_STATUS);

        Order order = new Order();
        order.setUser(userRepository.findUserByPublicKey(TEST_PUBKEY).get());
        order.setTotalTax(TEST_PLUSHTOY_TAX_AMOUNT);
        order.setTotalPrice(TEST_PLUSHTOY_PRICE);
        order.setTimestamp(TEST_ORDER_TIMESTAMP);
        order.setDeliveryStatus(TEST_ORDER_DELIVERY_STATUS);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setPricePerPiece(plushToy.getPrice());
        orderItem.setOrder(order);
        orderItem.setPlushToy(plushToy);
        orderItem.setAmount(TEST_ORDER_AMOUNT);
        orderItem.setPosition(TEST_ORDER_POSITION);
        orderItem.setImageUrl(plushToy.getImageUrl());
        orderItem.setName(plushToy.getName());
        orderItem.setTaxAmount(plushToy.getTaxAmount());
        orderItem.setTaxClass(plushToy.getTaxClass());
        orderItemRepository.save(orderItem);

        OrderItemListDto orderItemListDto = new OrderItemListDto();
        orderItemListDto.setPricePerPiece(TEST_PLUSHTOY_PRICE);
        orderItemListDto.setId(orderItem.getId());
        orderItemListDto.setAmount(TEST_ORDER_AMOUNT);
        orderItemListDto.setImageUrl(null);
        orderItemListDto.setPlushToyId(plushToy.getId());
        orderItemListDto.setName(TEST_PLUSHTOY_NAME);
        List<OrderItemListDto> orderItems = new ArrayList<>();
        orderItems.add(orderItemListDto);

        OrderListDto orderListDto = new OrderListDto();
        orderListDto.setDeliveryStatus(TEST_ORDER_DELIVERY_STATUS_STRING);
        orderListDto.setId(order.getId());
        orderListDto.setTotalTax(TEST_PLUSHTOY_TAX_AMOUNT);
        orderListDto.setTotalPrice(TEST_PLUSHTOY_PRICE);
        orderListDto.setTimestamp(TEST_ORDER_TIMESTAMP);
        orderListDto.setOrderItems(orderItems);

        List<OrderListDto> orderHistory = userService.getOrderHistory(TEST_PUBKEY);

        assertEquals(1, orderHistory.size());
        assertEquals(orderListDto.getId(), orderHistory.getFirst().getId());
        assertEquals(orderListDto.getDeliveryStatus(), orderHistory.getFirst().getDeliveryStatus());
        assertEquals(orderListDto.getTotalTax(), orderHistory.getFirst().getTotalTax());
        assertEquals(orderListDto.getTotalPrice(), orderHistory.getFirst().getTotalPrice());
        assertEquals(orderListDto.getTimestamp(), orderHistory.getFirst().getTimestamp());

        OrderItemListDto actualItem = orderHistory.getFirst().getOrderItems().getFirst();

        assertEquals(orderItem.getId(), actualItem.getId());
        assertEquals(orderItem.getPricePerPiece(), actualItem.getPricePerPiece());
        assertEquals(orderItem.getAmount(), actualItem.getAmount());
        assertEquals(orderItem.getImageUrl(), actualItem.getImageUrl());
        assertEquals(orderItem.getName(), actualItem.getName());
    }

    @Test
    public void givenInvalidPublicKey_whenGetOrderHistory_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.getOrderHistory(TEST_NONEXISTENT_PUBKEY));
    }
}
