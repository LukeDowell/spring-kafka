package dev.dowell.springkafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
@Slf4j
public class SpringKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaApplication.class, args);
    }

    @Bean
    public Function<String, String> receiveMemberInformation() {
        return in -> {
            log.info("Message received {}", in);
            return UUID.randomUUID().toString();
        };
    }
}
