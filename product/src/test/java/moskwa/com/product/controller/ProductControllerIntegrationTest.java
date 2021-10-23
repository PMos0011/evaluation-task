package moskwa.com.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import moskwa.com.product.ProductApplication;
import moskwa.com.product.annotations.ProductAfter;
import moskwa.com.product.model.ProductDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ProductApplication.class})
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @ProductAfter
    void shouldPersistProduct() throws Exception {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        assertEquals(1L, productRepository.findAll().size());
    }

    @Test
    @ProductAfter
    void shouldReturnConflictWhenProductExists() throws Exception {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        assertEquals(1L, productRepository.findAll().size());

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isConflict());

        assertEquals(1L, productRepository.findAll().size());
    }

    @Test
    void shouldReturnUnprocessableWhenProductHasNoCreditId() throws Exception {
        ProductDto product = ProductDto.builder()
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, productRepository.findAll().size());
    }

    @Test
    void shouldReturnUnprocessableWhenProductHasNoName() throws Exception {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .value(new BigDecimal(100))
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, productRepository.findAll().size());
    }

    @Test
    void shouldReturnUnprocessableWhenProductHasNoValue() throws Exception {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .productName("Credit 1")
                .build();

        mockMvc.perform(post("/create-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, productRepository.findAll().size());
    }

    @Test
    @ProductAfter
    void shouldCreateTwoProductsAndReturnTwoProducts() throws Exception {
        ProductDto productOne = ProductDto.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        ProductDto productTwo = ProductDto.builder()
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

        mockMvc.perform(get("/get-products?ids=1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
