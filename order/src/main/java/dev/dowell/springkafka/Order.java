package dev.dowell.springkafka;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "food_order") // Huh turns out you can't name a table 'order'
public class Order {

    @Id
    private UUID id;

    private String customerId;
}
