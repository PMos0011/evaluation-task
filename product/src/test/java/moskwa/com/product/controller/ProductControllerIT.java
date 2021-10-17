package moskwa.com.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import moskwa.com.product.ProductApplication;
import moskwa.com.product.annotations.ProductAfter;
import moskwa.com.product.model.Product;
import moskwa.com.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ProductApplication.class})
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @ProductAfter
    void shouldPersistCustomer() throws Exception {
        Product product = Product.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        assertThat(productRepository.findAll().size()).isEqualTo(1L);
    }

    @Test
    @ProductAfter
    void shouldReturnConflictWhenProductExists() throws Exception {
        Product product = Product.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        assertThat(productRepository.findAll().size()).isEqualTo(1L);

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isConflict());

        assertThat(productRepository.findAll().size()).isEqualTo(1L);
    }

    @Test
    void shouldReturnUnprocessableWhenProductIsInvalid() throws Exception {
        Product product = Product.builder()
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isUnprocessableEntity());

        assertThat(productRepository.findAll().size()).isEqualTo(0L);
    }

    @Test
    @ProductAfter
    void shouldCreateTwoProductsAndReturnTwoProducts() throws Exception {
        Product productOne = Product.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        Product productTwo = Product.builder()
                .creditId(2L)
                .productName("Credit 2")
                .value(new BigDecimal(200))
                .build();


        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productOne)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTwo)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/get-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
