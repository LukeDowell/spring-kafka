package dev.dowell.springkafka.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class RestaurantConfiguration {

    private static final ConcurrentHashMap<String, NewOrder> orderCache = new ConcurrentHashMap<>();

    @Bean
    public Function<NewOrder, NewOrder> newOrderCache() {
        return newOrder -> {
            if (!orderCache.containsKey(newOrder.getId())) {
                orderCache.put(newOrder.getId(), newOrder);
            }
            return newOrder;
        };
    }

    @Bean
    public IntegrationFlow newOrderFlow(NewOrderHandler newOrderHandler) {
        return IntegrationFlows.from(NewOrderConsumer.class, gateway -> gateway.beanName("receiveNewOrder"))
//            .scatterGather()  Stuff we could do with Spring Integration
//            .log()
//            .filter()
            .handle(newOrderHandler)
            .get();
    }

    @Bean
    public NewOrderHandler restaurantNewOrderHandler(RestaurantOrderRepository repository,
                                                     StreamBridge streamBridge) {
        return new NewOrderHandler(repository, streamBridge);
    }

    public interface NewOrderConsumer extends Consumer<Message<NewOrder>> {

    }
}
