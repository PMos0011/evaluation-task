package moskwa.com.credit;

import moskwa.com.credit.rest.TestCustomerRESTService;
import moskwa.com.credit.rest.TestProductRESTService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {CreditApplication.class, BaseIntegrationTest.ClientsConfiguration.class})
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    @TestConfiguration
    public static class ClientsConfiguration {
        @Bean
        @Primary
        public TestCustomerRESTService testCustomerClient() {
            return new TestCustomerRESTService();
        }

        @Bean
        @Primary
        public TestProductRESTService testProductClient() {
            return new TestProductRESTService();
        }
    }
}
