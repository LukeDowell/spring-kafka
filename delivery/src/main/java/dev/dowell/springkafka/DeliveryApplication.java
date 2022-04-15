package dev.dowell.springkafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
@Slf4j
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }

    @Bean
    public Function<String, String> receiveMemberInformation() {
        return in -> {
            log.info("Message received {}", in);
            return UUID.randomUUID().toString();
        };
    }
}
