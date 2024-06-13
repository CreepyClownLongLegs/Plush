package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.DeliveryStatus;
import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import at.ac.tuwien.sepr.groupphase.backend.repository.DeliveryStatusRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;

@Profile("generateData")
@Component
@DependsOn({"adminDataGenerator", "plushToyDataGenerator"})
public class OrderDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_TO_GENERATE = 5;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;

    public OrderDataGenerator(OrderRepository orderRepository, UserRepository userRepository, DeliveryStatusRepository deliveryStatusRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.deliveryStatusRepository = deliveryStatusRepository;
    }

    @PostConstruct
    private void generate() {
        if (!orderRepository.findAll().isEmpty()) {
            LOGGER.debug("order already generated");
        } else {
            LOGGER.debug("generating {} order entries", NUMBER_TO_GENERATE);
            for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
                Order order = new Order();

                if (i % 2 == 0) {
                    DeliveryStatus deliveryStatus = new DeliveryStatus(2, "pending");
                    deliveryStatusRepository.save(deliveryStatus);
                    order.setDeliveryStatus(deliveryStatus);
                } else {
                    DeliveryStatus deliveryStatus = new DeliveryStatus(3, "delivered");
                    deliveryStatusRepository.save(deliveryStatus);
                    order.setDeliveryStatus(deliveryStatus);
                }

                order.setTotalPrice(22L);
                order.setTotalTax(2L);
                order.setTimestamp(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
                order.setUser(userRepository.findUserByPublicKey("HGXLg2Eo9hUu7NGWkvVMrTzmjfwC2y1jGw25knAep4Gq").orElseThrow());
                LOGGER.debug("saving order {}", order);
                orderRepository.save(order);
                LOGGER.info("created order with ID: {}", order.getId());
            }

        }
    }

}
