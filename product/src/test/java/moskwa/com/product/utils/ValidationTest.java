package moskwa.com.product.utils;

import moskwa.com.product.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {
    @Test
    void shouldReturnTrueWhenProductEntityIsValid() {
        Product product = Product.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        assertTrue(ProductValidation.validateProduct(product));
    }

    @Test
    void shouldReturnFalseWhenCreditIdMissing() {
        Product product = Product.builder()
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        assertFalse(ProductValidation.validateProduct(product));
    }

    @Test
    void shouldReturnFalseWhenProductNameIsempty() {
        Product product = Product.builder()
                .creditId(1L)
                .productName(" ")
                .value(new BigDecimal(100))
                .build();

        assertFalse(ProductValidation.validateProduct(product));
    }

    @Test
    void shouldReturnFalseWhenCProductValueMissing() {
        Product product = Product.builder()
                .creditId(1L)
                .productName(" ")
                .build();

        assertFalse(ProductValidation.validateProduct(product));
    }
}
