package moskwa.com.product.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
public class ProductDto {
    @NotNull
    private final Long creditId;
    @NotEmpty
    private final String productName;
    @NotNull
    private final BigDecimal value;
}
