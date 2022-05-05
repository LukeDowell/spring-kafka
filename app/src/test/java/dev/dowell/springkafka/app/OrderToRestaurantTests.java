package dev.dowell.springkafka.app;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfigureMockMvc
@Import(TestChannelBinderConfiguration.class)
@SpringBootTest
public class OrderToRestaurantTests extends AbstractIntegrationTest {

    // TODO Hit Order API and await for Restaurant to persist a RestaurantOrder with the status of "Prepping"
}
