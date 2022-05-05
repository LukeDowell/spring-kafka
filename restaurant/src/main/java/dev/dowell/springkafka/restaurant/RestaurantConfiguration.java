package dev.dowell.springkafka.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class RestaurantConfiguration {

    @Bean
    public IntegrationFlow newOrderFlow(RestaurantNewOrderHandler newOrderHandler) {
        return IntegrationFlows.from(NewOrderConsumer.class, gateway -> gateway.beanName("receiveNewOrder"))
            .handle(newOrderHandler)
            .get();
    }

    @Bean
    public RestaurantNewOrderHandler restaurantNewOrderHandler(ObjectMapper objectMapper) {
        return new RestaurantNewOrderHandler(objectMapper);
    }

    public interface NewOrderConsumer extends Consumer<Message<RestaurantNewOrder>> {

    }
}
