package moskwa.com.credit.rest;

import lombok.RequiredArgsConstructor;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.service.CreditServiceFailure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static moskwa.com.credit.service.CreditServiceFailure.PERSIST_FAILURE;

@Component
@RequiredArgsConstructor
public class CustomerRESTService implements CustomerClient {
    @Value("${customer-service-url}")
    private String customerServiceUrl;
    private final String CREATE_CUSTOMER_ENDPOINT = "/create-customer";
    private final String GET_CUSTOMER_ENDPOINT = "/get-customers";
    private final String DELETE_CUSTOMER_ENDPOINT = "/customer";

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    @Override
    public Optional<CreditServiceFailure> createCustomerOrGetFailure(CustomerRESTDto customerRESTDto) {
        try {
            String url = customerServiceUrl.concat(CREATE_CUSTOMER_ENDPOINT);
            HttpEntity<CustomerRESTDto> entity = new HttpEntity<>(customerRESTDto, headers);
            restTemplate.postForEntity(url, entity, Void.class);
            return Optional.empty();
        } catch (HttpClientErrorException httpClientErrorException) {
            return Optional.of(PERSIST_FAILURE);
        }
    }

    @Override
    public Optional<List<CustomerRESTDto>> getCustomers() {
        try {
            String url = customerServiceUrl.concat(GET_CUSTOMER_ENDPOINT);
            HttpEntity<CustomerRESTDto> entity = new HttpEntity<>(headers);
            return Optional.of(Arrays.stream(restTemplate.exchange(url, HttpMethod.GET, entity, CustomerRESTDto[].class).getBody())
                    .collect(Collectors.toList()));
        } catch (HttpClientErrorException httpClientErrorException) {
            return Optional.empty();
        }
    }

    @Override
    public void revertCreatedCustomer(CustomerRESTDto customerRESTDto) {
        try {
            String url = customerServiceUrl.concat(DELETE_CUSTOMER_ENDPOINT);
            HttpEntity<CustomerRESTDto> entity = new HttpEntity<>(customerRESTDto, headers);
            restTemplate.delete(url, entity, Void.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            // Here should be reverting error handling - omitted in evaluation task
            System.out.printf("Error while reverting customer with creditId: %d%n", customerRESTDto.getCreditId());
        }
    }
}
