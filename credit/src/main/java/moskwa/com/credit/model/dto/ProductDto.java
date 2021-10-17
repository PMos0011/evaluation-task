package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
public class ProductDto {
    @NotBlank
    private final String productName;
    @NotNull
    private final BigDecimal value;
}
