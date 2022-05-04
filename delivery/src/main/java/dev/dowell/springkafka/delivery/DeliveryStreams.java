package dev.dowell.springkafka.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Function;

@Configuration
@Slf4j
public class DeliveryStreams {

    @Bean
    public Function<String, String> receiveMemberInformation() {
        return in -> {
            log.info("Message received {}", in);
            return UUID.randomUUID().toString();
        };
    }
}
