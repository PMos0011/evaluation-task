package moskwa.com.product.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductDto {
    private final Long creditId;
    private final String productName;
    private final BigDecimal value;
}
