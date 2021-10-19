package moskwa.com.credit.utils;

import moskwa.com.credit.model.Credit;
import moskwa.com.credit.model.dto.CreditDto;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.model.dto.CustomerDto;
import moskwa.com.credit.model.dto.ProductDto;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.model.dto.ProductRESTDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CreditMapper {

    public Optional<CreditRequestDto> mapToCreditRequestDto(Credit credit, List<CustomerRESTDto> customers, List<ProductRESTDto> products) {
        Optional<CustomerDto> customerDto = resolveCustomer(customers, credit.getId());
        Optional<ProductDto> productDto = resolveProduct(products, credit.getId());

        if (customerDto.isPresent() && productDto.isPresent())
            return Optional.of(CreditRequestDto.builder()
                    .credit(new CreditDto(credit.getCreditName()))
                    .customer(customerDto.get())
                    .product(productDto.get())
                    .build());

        return Optional.empty();
    }

    private Optional<CustomerDto> resolveCustomer(List<CustomerRESTDto> customers, final long creditId) {
        return customers.stream().filter(customer -> customer.getCreditId() == creditId)
                .findFirst()
                .map(CustomerRESTDto::toDto);
    }

    private Optional<ProductDto> resolveProduct(List<ProductRESTDto> products, final long creditId) {
        return products.stream().filter(product -> product.getCreditId() == creditId)
                .findFirst()
                .map(ProductRESTDto::toDto);
    }
}
