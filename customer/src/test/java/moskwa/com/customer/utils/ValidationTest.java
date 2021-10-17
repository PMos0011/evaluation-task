package moskwa.com.customer.utils;

import moskwa.com.customer.model.CustomerDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {

    @Test
    void shouldReturnTrueWhenCustomerEntityIsValid() {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        assertTrue(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCreditIdMissing() {
        CustomerDto customer = CustomerDto.builder()
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCustomerNameMissing() {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCustomerSurnameIsEmpty() {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("   ")
                .pesel("01234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCustomerPESELisInvalid() {
        CustomerDto customer = CustomerDto.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("A1234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }
}
