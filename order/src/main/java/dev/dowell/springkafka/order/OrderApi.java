package dev.dowell.springkafka.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    @PostMapping
    public <T> ResponseEntity<T> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        var savedEntity = orderRepository.save(
            Order.builder()
                .id(UUID.randomUUID())
                .customerId(createOrderRequest.customerId)
                .build()
        );

        // TODO transactional, handle case of StreamBridge#send returning false
        streamBridge.send("newOrder-out-0", savedEntity);

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
