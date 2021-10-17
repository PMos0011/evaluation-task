package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductDto {
    private final String productName;
    private final BigDecimal value;
}
