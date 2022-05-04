package dev.dowell.springkafka.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestChannelBinderConfiguration.class)
public class DeliveryEventTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine3.15")
        .withUsername("test_user")
        .withPassword("test_password")
        .withReuse(true);

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("a new order should trigger a delivery")
    void newOrderTriggersDelivery() {
        var orderId = UUID.randomUUID().toString();
        var customerId = UUID.randomUUID().toString();
        var outgoingMessage = MessageBuilder.withPayload(
            "{" +
                " \"orderId\": " + orderId +
                " \"customerId\": " + customerId +
                "}"
        ).build();

        inputDestination.send(outgoingMessage, "order-out-new-order");

        var message = outputDestination.receive(15_000L, "delivery-out-new-delivery").getPayload();
        var payload = Configuration.defaultConfiguration().jsonProvider().parse(new String(message));

        assertThat(JsonPath.<String>read(payload, "$.delivery")).isNotEmpty();
        assertThat(JsonPath.<String>read(payload, "$.delivery.id")).isNotEmpty();
        assertThat(JsonPath.<String>read(payload, "$.delivery.orderId")).isEqualTo(orderId);
    }
}
