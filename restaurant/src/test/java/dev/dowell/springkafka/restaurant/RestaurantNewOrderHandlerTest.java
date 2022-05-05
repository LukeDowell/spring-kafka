package dev.dowell.springkafka.restaurant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class RestaurantNewOrderHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private InputDestination inputDestination;

    @Test
    @DisplayName("it should handle a restaurant new order event")
    public void handle() {
        var payload = "{\n" +
            "  \"id\": \"abc1234\",\n" +
            "  \"customerId\": \"1234\"\n" +
            "}";

        var message = MessageBuilder.withPayload(payload).build();
        inputDestination.send(message, "new-order");
    }
}
