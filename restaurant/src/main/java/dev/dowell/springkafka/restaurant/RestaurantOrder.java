package dev.dowell.springkafka.restaurant;

import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurant_order")
public class RestaurantOrder {

    @Id
    private String id;

    private String customerId;

    private Status status;

    enum Status {
        PREP,
        CANCELLED,
        READY_FOR_PICKUP
    }
}
