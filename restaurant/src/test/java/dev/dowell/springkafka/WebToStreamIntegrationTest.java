package dev.dowell.springkafka;

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

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@AutoConfigureMockMvc
public class WebToStreamIntegrationTest {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("it should be able to submit a message to a channel from a web call")
    public void webToKafka() throws Exception {
        mockMvc.perform(post("/api/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\n" +
                    "  \"referralId\": \"1\",\n" +
                    "  \"firstName\": \"New\",\n" +
                    "  \"lastName\": \"Member\"\n" +
                "}"
            )).andExpect(status().isOk());

        var payload = outputDestination.receive(1000L, "funnel-out-new-member").getPayload();
        var id = new String(payload);
        assertThat(UUID.fromString(id)).isNotNull();
    }
}
