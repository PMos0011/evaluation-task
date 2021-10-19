package moskwa.com.credit.rest;

import lombok.RequiredArgsConstructor;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.model.dto.ProductRESTDto;
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
public class ProductRESTService implements ProductClient {
    private final static String CREATE_PRODUCT_ENDPOINT = "/create-product";
    private final static String GET_PRODUCTS_ENDPOINT = "/get-products?ids=%s";

    @Value("${product-service-url}")
    private String productServiceUrl;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    @Override
    public Optional<CreditServiceFailure> createProductOrGetFailure(ProductRESTDto productRESTDto) {
        try {
            String url = productServiceUrl.concat(CREATE_PRODUCT_ENDPOINT);
            HttpEntity<ProductRESTDto> entity = new HttpEntity<>(productRESTDto, headers);
            restTemplate.postForEntity(url, entity, Void.class);
            return Optional.empty();
        } catch (HttpClientErrorException httpClientErrorException) {
            return Optional.of(PERSIST_FAILURE);
        }
    }

    @Override
    public Optional<List<ProductRESTDto>> getProducts(String ids) {
        try {
            String url = productServiceUrl.concat(String.format(GET_PRODUCTS_ENDPOINT, ids));
            HttpEntity<CustomerRESTDto> entity = new HttpEntity<>(headers);
            return Optional.of(Arrays.stream(restTemplate.exchange(url, HttpMethod.GET, entity, ProductRESTDto[].class).getBody())
                    .collect(Collectors.toList()));
        } catch (HttpClientErrorException httpClientErrorException) {
            return Optional.empty();
        }
    }
}
