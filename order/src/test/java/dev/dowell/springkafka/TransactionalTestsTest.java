package dev.dowell.springkafka;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionalTestsTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void shouldSaveAndRetrieveAnOrder() {
        var id = UUID.randomUUID();
        var myCustomerId = UUID.randomUUID().toString();
        var order = Order.builder()
            .id(id)
            .customerId(myCustomerId)
            .build();

        orderRepository.save(order);

        assertThat(orderRepository.findByCustomerId(myCustomerId).get()).isEqualTo(order);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void shouldDeleteByCustomerId() {
        var id = UUID.randomUUID();
        var myCustomerId = UUID.randomUUID().toString();
        var order = Order.builder()
            .id(id)
            .customerId(myCustomerId)
            .build();

        orderRepository.save(order);
        assertThat(orderRepository.findAll()).anyMatch((o) -> o.getCustomerId().equals(myCustomerId));

        orderRepository.deleteByCustomerId(myCustomerId);
        assertThat(orderRepository.findAll()).isEmpty();
    }
}
