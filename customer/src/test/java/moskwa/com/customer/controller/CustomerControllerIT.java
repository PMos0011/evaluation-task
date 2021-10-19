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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

        assertEquals(1L, customerRepository.findAll().size());
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

        assertEquals(1L, customerRepository.findAll().size());
    }

    @Test
    void shouldReturnUnprocessableWhenCustomerHasNoCreditId() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, customerRepository.findAll().size());
    }


    @Test
    void shouldReturnUnprocessableWhenCustomerHasNoFirstName() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, customerRepository.findAll().size());
    }

    @Test
    void shouldReturnUnprocessableWhenCustomerHasNoSurname() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .pesel("01234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, customerRepository.findAll().size());
    }

    @Test
    void shouldReturnUnprocessableWhenCustomerHasInvalidPESEL() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("A1234567890")
                .build();

        mockMvc.perform(post("/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0L, customerRepository.findAll().size());
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

        mockMvc.perform(get("/get-customers?ids=1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
