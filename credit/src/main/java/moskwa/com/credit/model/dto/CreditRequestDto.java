package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditRequestDto {
    private final CreditDto credit;
    private final CustomerDto customer;
    private final ProductDto product;
}
