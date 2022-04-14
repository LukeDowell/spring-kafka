package dev.dowell.springkafka;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;

@Data
@Setter(AccessLevel.PRIVATE)
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;

    private Status status = Status.UNSTARTED;

    public Order() {
        
    }

    enum Status {
        UNSTARTED,
        STARTED,
        COMPLETED
    }
}
