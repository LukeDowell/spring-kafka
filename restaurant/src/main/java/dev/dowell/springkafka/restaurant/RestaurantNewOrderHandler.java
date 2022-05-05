package dev.dowell.springkafka.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class RestaurantNewOrderHandler implements MessageHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(@NonNull Message<?> message) {
        try {
            var newOrder = objectMapper.readValue((byte[]) message.getPayload(), RestaurantNewOrder.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
