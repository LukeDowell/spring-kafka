package dev.dowell.springkafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class MessagingIntegrationTest {

    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @Test
    @DisplayName("it should be able to send a message through the bound message broker")
    void canSendMessage() {
        input.send(MessageBuilder.withPayload("bla bla").build(), "hello-in-0");
        var message = new String(output.receive(1000L, "hello-out-0").getPayload());
        assertThat(message).isEqualTo("General Kenobi");
    }
}
