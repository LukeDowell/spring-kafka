package dev.dowell.springkafka;

import lombok.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApi {

    @NonNull private final OrderRepository orderRepository;
    @NonNull private final StreamBridge streamBridge;

    @PostMapping
//    @Transactional
    public <T> ResponseEntity<T> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        var savedEntity = orderRepository.save(
            Order.builder()
                .id(UUID.randomUUID())
                .customerId(createOrderRequest.customerId)
                .build()
        );

        streamBridge.send("order-out-order-created", savedEntity);

        return ResponseEntity.ok().build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateOrderRequest {
        private String restaurantName;
        private String restaurantAddress;
        private Object cartItems;
        private String customerId;
    }
}
