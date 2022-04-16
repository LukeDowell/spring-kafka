package dev.dowell.springkafka;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class OrderApiTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    @DisplayName("it should be able to submit an order and have it persisted")
    void submitOrder() throws Exception {
        var customerId = UUID.randomUUID().toString();
        mockMvc.perform(post("/api/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\n" +
                    "  \"restaurantName\": \"\",\n" +
                    "  \"restaurantAddress\": \"\",\n" +
                    "  \"cartItems\": [\n" +
                    "    {\n" +
                    "      \"name\": \"Lentil Soup\",\n" +
                    "      \"price\": 3.50\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"customerId\": \"" + customerId + "\"\n" +
                    "}"
            ))
            .andExpect(status().isOk());

        assertThat(orderRepository.findByCustomerId(customerId).isPresent()).isTrue();
    }

    @Test
    @DisplayName("it should be able to submit an order and emit an event")
    public void submitOrderAndSeeEvent() throws Exception {
        var customerId = UUID.randomUUID().toString();
        mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n" +
                        "  \"restaurantName\": \"\",\n" +
                        "  \"restaurantAddress\": \"\",\n" +
                        "  \"cartItems\": [\n" +
                        "    {\n" +
                        "      \"name\": \"Lentil Soup\",\n" +
                        "      \"price\": 3.50\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"customerId\": \"" + customerId + "\"\n" +
                        "}"
                ))
            .andExpect(status().isOk());

        var message = outputDestination.receive(1000L, "order-out-order-created").getPayload();
        var payload = Configuration.defaultConfiguration().jsonProvider().parse(new String(message));

        assertThat(JsonPath.<String>read(payload, "$.id")).isNotEmpty();
        assertThat(JsonPath.<String>read(payload, "$.customerId")).isEqualTo(customerId);
    }
}
