package dev.dowell.springkafka;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @PostMapping
    public ResponseEntity<Void> createRestaurant() {
        return ResponseEntity.ok().build();
    }
}
