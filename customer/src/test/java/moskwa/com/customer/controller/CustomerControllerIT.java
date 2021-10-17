package moskwa.com.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import moskwa.com.customer.CustomerApplication;
import moskwa.com.customer.annotations.CustomerAfter;
import moskwa.com.customer.model.CustomerDto;
import moskwa.com.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {CustomerApplication.class})
public class CustomerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @CustomerAfter
    void shouldPersistCustomer() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());

        assertThat(customerRepository.findAll().size()).isEqualTo(1L);
    }

    @Test
    @CustomerAfter
    void shouldReturnConflictWhenCustomerExists() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isConflict());

        assertThat(customerRepository.findAll().size()).isEqualTo(1L);
    }

    @Test
    void shouldReturnUnprocessableWhenCustomerIsInvalid() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isUnprocessableEntity());

        assertThat(customerRepository.findAll().size()).isEqualTo(0L);
    }

    @Test
    @CustomerAfter
    void shouldCreateTwoCustomersAndReturnTwoCustomers() throws Exception {
        CustomerDto customerOne = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        CustomerDto customerTwo = CustomerDto.builder()
                .creditId(2L)
                .firstName("Rocky")
                .surname("Balboa")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerOne)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerTwo)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/get-customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldRevertCustomer() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());

        assertThat(customerRepository.findAll().size()).isEqualTo(1L);

        mockMvc.perform(delete("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk());

        assertThat(customerRepository.findAll().size()).isEqualTo(0L);
    }
}
