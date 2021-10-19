package moskwa.com.credit;

import moskwa.com.credit.model.dto.CreditDto;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.model.dto.CustomerDto;
import moskwa.com.credit.model.dto.ProductDto;

import java.math.BigDecimal;

public class CreditRequestDtoBuilder {

    public static CreditRequestDto createCreditRequestDto(final int number) {
        return CreditRequestDto.builder()
                .credit(new CreditDto(String.format("CommonName_%d", number)))
                .customer(CustomerDto.builder()
                        .firstName("John")
                        .surname(String.format("CommonName_%d", number))
                        .pesel("01234567890")
                        .build())
                .product(ProductDto.builder()
                        .productName(String.format("CommonName_%d", number))
                        .value(new BigDecimal(100))
                        .build())
                .build();
    }

    public static CreditRequestDto createWithoutCreditName() {
        return CreditRequestDto.builder()
                .credit(new CreditDto())
                .customer(CustomerDto.builder()
                        .firstName("John")
                        .surname("Rambo")
                        .pesel("01234567890")
                        .build())
                .product(ProductDto.builder()
                        .productName("Name")
                        .value(new BigDecimal(100))
                        .build())
                .build();
    }

    public static CreditRequestDto createWithoutProduct() {
        return CreditRequestDto.builder()
                .credit(new CreditDto("Credit 1"))
                .customer(CustomerDto.builder()
                        .firstName("John")
                        .surname("Rambo")
                        .pesel("01234567890")
                        .build())
                .build();
    }

    public static CreditRequestDto createWithoutCustomer() {
        return CreditRequestDto.builder()
                .credit(new CreditDto("Credit 1"))
                .product(ProductDto.builder()
                        .productName("Name")
                        .value(new BigDecimal(100))
                        .build())
                .build();
    }
}
