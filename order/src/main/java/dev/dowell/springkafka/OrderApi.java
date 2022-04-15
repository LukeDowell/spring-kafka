package dev.dowell.springkafka;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/order")
public class OrderApi {

    @PostMapping
    public <T> ResponseEntity<T> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
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
