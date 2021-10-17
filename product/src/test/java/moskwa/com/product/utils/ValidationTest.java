package moskwa.com.product.utils;

import moskwa.com.product.model.ProductDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {
    @Test
    void shouldReturnTrueWhenProductEntityIsValid() {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        assertTrue(ProductValidation.validateProduct(product));
    }

    @Test
    void shouldReturnFalseWhenCreditIdMissing() {
        ProductDto product = ProductDto.builder()
                .productName("Credit 1")
                .value(new BigDecimal(100))
                .build();

        assertFalse(ProductValidation.validateProduct(product));
    }

    @Test
    void shouldReturnFalseWhenProductNameIsEmpty() {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .productName(" ")
                .value(new BigDecimal(100))
                .build();

        assertFalse(ProductValidation.validateProduct(product));
    }

    @Test
    void shouldReturnFalseWhenCProductValueMissing() {
        ProductDto product = ProductDto.builder()
                .creditId(1L)
                .productName(" ")
                .build();

        assertFalse(ProductValidation.validateProduct(product));
    }
}
