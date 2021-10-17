package moskwa.com.credit.utils;

import moskwa.com.credit.model.Credit;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.model.dto.ProductRESTDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditMapperTest {

    CreditMapper creditMapper = new CreditMapper();

    List<CustomerRESTDto> customerRESTDtos = Arrays.asList(
            CustomerRESTDto.builder()
                    .creditId(1L)
                    .firstName("John")
                    .surname("Name1")
                    .pesel("01234567890")
                    .build(),
            CustomerRESTDto.builder()
                    .creditId(2L)
                    .firstName("John")
                    .surname("Name2")
                    .pesel("01234567890")
                    .build(),
            CustomerRESTDto.builder()
                    .creditId(3L)
                    .firstName("John")
                    .surname("Name3")
                    .pesel("01234567890")
                    .build()
    );

    List<ProductRESTDto> productRESTDtos = Arrays.asList(
            ProductRESTDto.builder()
                    .creditId(1L)
                    .productName("Name1")
                    .value(new BigDecimal(100))
                    .build(),
            ProductRESTDto.builder()
                    .creditId(2L)
                    .productName("Name2")
                    .value(new BigDecimal(100))
                    .build(),
            ProductRESTDto.builder()
                    .creditId(3L)
                    .productName("Name3")
                    .value(new BigDecimal(100))
                    .build()
    );

    @Test
    public void shouldReturnOptionalCreditRequestDto() {
        Credit credit = new Credit(2L, "Name2");

        Optional<CreditRequestDto> result = creditMapper.mapToCreditRequestDto(credit, customerRESTDtos, productRESTDtos);
        assertTrue(result.isPresent());

        result.ifPresent(creditRequestDto -> {
            assertEquals(creditRequestDto.getCredit().getCreditName(), creditRequestDto.getCustomer().getSurname());
            assertEquals(creditRequestDto.getCredit().getCreditName(), creditRequestDto.getProduct().getProductName());
        });
    }

    @Test
    public void shouldReturnOptionalEmpty() {
        Credit credit = new Credit(4L, "Name1");

        Optional<CreditRequestDto> result = creditMapper.mapToCreditRequestDto(credit, customerRESTDtos, productRESTDtos);
        assertTrue(result.isEmpty());
    }
}
