package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductRESTDto {
    private final long creditId;
    private final String productName;
    private final BigDecimal value;

    public static ProductRESTDto createRESTDto(long creditId, ProductDto productDto) {
        return ProductRESTDto.builder()
                .creditId(creditId)
                .productName(productDto.getProductName())
                .value(productDto.getValue())
                .build();
    }

    public ProductDto toDto(){
        return ProductDto.builder()
                .productName(productName)
                .value(value)
                .build();
    }
}
