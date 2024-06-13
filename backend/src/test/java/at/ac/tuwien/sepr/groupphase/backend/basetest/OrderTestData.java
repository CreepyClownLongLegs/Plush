package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.entity.DeliveryStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface OrderTestData {
    String BASE_URI = "/api/v1";
    String ORDERS_BASE_URI = BASE_URI + "/user/orders";
    LocalDateTime TEST_ORDER_TIMESTAMP = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
    String TEST_ORDER_DELIVERY_STATUS_STRING = "delivered";
    DeliveryStatus TEST_ORDER_DELIVERY_STATUS = new DeliveryStatus(3, TEST_ORDER_DELIVERY_STATUS_STRING);
    int TEST_ORDER_AMOUNT = 1;
    int TEST_ORDER_POSITION = 1;
}

