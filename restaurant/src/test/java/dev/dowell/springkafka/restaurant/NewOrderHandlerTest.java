package dev.dowell.springkafka.restaurant;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class NewOrderHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    @DisplayName("it should handle a restaurant new order event")
    public void handle() {
        var customerId = "1234";
        var outgoingMessage = MessageBuilder.withPayload(
            "{\n" +
                "  \"id\": \"abc1234\",\n" +
                "  \"customerId\": \"" + customerId + "\"\n" +
                "}"
        ).build();

        inputDestination.send(outgoingMessage, "new-order");

        var message = outputDestination.receive(1000L, "restaurant-order-prep-started").getPayload();
        var payload = Configuration.defaultConfiguration().jsonProvider().parse(new String(message));

        assertThat(JsonPath.<String>read(payload, "$.restaurantOrderId")).isNotEmpty();
        assertThat(JsonPath.<String>read(payload, "$.timePrepStarted")).isNotEmpty();
        assertThat(JsonPath.<String>read(payload, "$.customerId")).isEqualTo(customerId);
    }

    @Test
    @DisplayName("it should discard orders it has already received")
    public void idempotence() {
        var customerId = "1234";
        var outgoingMessage = MessageBuilder.withPayload(
            "{\n" +
                "  \"id\": \"abc1234\",\n" +
                "  \"customerId\": \"" + customerId + "\"\n" +
                "}"
        ).build();

        inputDestination.send(outgoingMessage, "new-order");
        outputDestination.receive(1000L, "restaurant-order-prep-started").getPayload();

        // DO IT AGAIN
        inputDestination.send(outgoingMessage, "new-order");
        assertThrows(
            TimeoutException.class,
            () -> outputDestination.receive(1000L, "restaurant-order-prep-started").getPayload()
        );
    }
}
