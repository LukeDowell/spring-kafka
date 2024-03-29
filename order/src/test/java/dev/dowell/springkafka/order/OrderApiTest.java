package dev.dowell.springkafka.order;

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
@Import(TestChannelBinderConfiguration.class)
@SpringBootTest
public class OrderApiTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    @DisplayName("it should be able to submit an order and emit a new-order event")
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

        var message = outputDestination.receive(1000L, "new-order").getPayload();
        var payload = Configuration.defaultConfiguration().jsonProvider().parse(new String(message));

        assertThat(JsonPath.<String>read(payload, "$.id")).isNotEmpty();
        assertThat(JsonPath.<String>read(payload, "$.customerId")).isEqualTo(customerId);
    }
}
