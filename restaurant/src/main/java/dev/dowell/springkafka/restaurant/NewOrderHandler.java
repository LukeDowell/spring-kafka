package dev.dowell.springkafka.restaurant;

import lombok.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.time.ZonedDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class NewOrderHandler implements MessageHandler {

    private final RestaurantOrderRepository restaurantOrderRepository;
    private final StreamBridge streamBridge;

    @Override
    public void handleMessage(@NonNull Message<?> message) {
        var newOrder = (NewOrder) message.getPayload();
        var restaurantOrder = RestaurantOrder.builder()
            .id(UUID.randomUUID().toString())
            .customerId(newOrder.getCustomerId())
            .status(RestaurantOrder.Status.PREP)
            .build();

        var savedOrder = restaurantOrderRepository.save(restaurantOrder);

        streamBridge.send(
            "receiveNewOrder-out-0",
            OrderPrepStarted.builder()
                .restaurantOrderId(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .timePrepStarted(ZonedDateTime.now()) // Uses timezone of server...
                .build()
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class OrderPrepStarted {
        private String restaurantOrderId;
        private String customerId;
        private ZonedDateTime timePrepStarted;
    }
}
