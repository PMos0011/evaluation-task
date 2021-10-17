package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreditRequestDto {
    @NotNull
    private final CreditDto credit;
    @NotNull
    private final CustomerDto customer;
    @NotNull
    private final ProductDto product;
}
