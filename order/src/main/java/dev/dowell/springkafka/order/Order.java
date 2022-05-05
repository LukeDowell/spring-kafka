package dev.dowell.springkafka.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer_order") // Huh turns out you can't name a table 'order'
public class Order {

    @Id
    private UUID id;

    private String customerId;

    private ZonedDateTime timeOrderWasPlaced;
}
