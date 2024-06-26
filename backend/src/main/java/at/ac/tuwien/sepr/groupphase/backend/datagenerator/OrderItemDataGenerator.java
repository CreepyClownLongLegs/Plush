package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import at.ac.tuwien.sepr.groupphase.backend.entity.OrderItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
@DependsOn({"plushToyDataGenerator", "orderDataGenerator"})
public class OrderItemDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_TO_GENERATE = 5;

    private final OrderItemRepository orderItemRepository;
    private final PlushToyRepository plushToyRepository;
    private final OrderRepository orderRepository;

    public OrderItemDataGenerator(OrderItemRepository orderItemRepository, PlushToyRepository plushToyRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.plushToyRepository = plushToyRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    private void generate() {
        if (!orderItemRepository.findAll().isEmpty()) {
            LOGGER.debug("order items already generated");
        } else {
            LOGGER.debug("generating {} order item entries", NUMBER_TO_GENERATE);
            for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
                Long longValue = (long) i + 1;
                PlushToy plushToy = plushToyRepository.findById(longValue).orElseThrow();
                Order order = orderRepository.findOrderById(longValue).orElseThrow();

                OrderItem item = new OrderItem();
                item.setAmount((int) (Math.random() * 10));
                item.setPosition(1);
                item.setOrder(order);
                item.setImageUrl(plushToy.getImageUrl());
                item.setTaxAmount(plushToy.getTaxAmount());
                item.setTaxClass(plushToy.getTaxClass());
                item.setName(plushToy.getName());
                item.setPlushToy(plushToy);
                item.setPricePerPiece(plushToy.getPrice());
                LOGGER.debug("saving order item {}", item);
                orderItemRepository.save(item);
                LOGGER.info("created order with ID: {}", item.getId());

                if (i == 2) {
                    OrderItem item2 = new OrderItem();
                    item2.setAmount((int) (Math.random() * 10));
                    item2.setPosition(2);
                    item2.setOrder(order);
                    PlushToy plushToy2 = plushToyRepository.findById(3L).orElseThrow();
                    item2.setImageUrl(plushToy2.getImageUrl());
                    item2.setTaxAmount(plushToy2.getTaxAmount());
                    item2.setTaxClass(plushToy2.getTaxClass());
                    item2.setName(plushToy2.getName());
                    item2.setPlushToy(plushToy2);
                    item2.setPricePerPiece(plushToy2.getPrice());

                    LOGGER.debug("saving order item {}", item2);
                    orderItemRepository.save(item2);
                    LOGGER.info("created order with ID: {}", item2.getId());

                    OrderItem item3 = new OrderItem();
                    item3.setAmount((int) (Math.random() * 10));
                    item3.setPosition(3);
                    item3.setOrder(order);
                    PlushToy plushToy3 = plushToyRepository.findById(4L).orElseThrow();
                    item3.setImageUrl(plushToy3.getImageUrl());
                    item3.setTaxAmount(plushToy3.getTaxAmount());
                    item3.setTaxClass(plushToy3.getTaxClass());
                    item3.setName(plushToy3.getName());
                    item3.setPlushToy(plushToy3);
                    item3.setPricePerPiece(plushToy3.getPrice());

                    LOGGER.debug("saving order item {}", item3);
                    orderItemRepository.save(item3);
                    LOGGER.info("created order with ID: {}", item3.getId());
                }

            }

        }
    }

}
