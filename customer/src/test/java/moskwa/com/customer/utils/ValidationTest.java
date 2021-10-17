package moskwa.com.customer.utils;

import moskwa.com.customer.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {

    @Test
    void shouldReturnTrueWhenCustomerEntityIsValid(){
        Customer customer = Customer.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        assertTrue(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCreditIdMissing(){
        Customer customer = Customer.builder()
                .firstName("John")
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCustomerNameMissing(){
        Customer customer = Customer.builder()
                .creditId(1L)
                .surname("Rambo")
                .pesel("01234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCustomerSurnameIsEmpty(){
        Customer customer = Customer.builder()
                .creditId(1L)
                .firstName("John")
                .surname("   ")
                .pesel("01234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }

    @Test
    void shouldReturnFalseWhenCustomerPESELisInvalid(){
        Customer customer = Customer.builder()
                .creditId(1L)
                .firstName("John")
                .surname("Rambo")
                .pesel("A1234567890")
                .build();

        assertFalse(CustomerValidation.validateCustomer(customer));
    }
}
