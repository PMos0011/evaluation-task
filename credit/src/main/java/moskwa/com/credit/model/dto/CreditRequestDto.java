package moskwa.com.credit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestDto {
    @NotNull
    private CreditDto credit;
    @NotNull
    private CustomerDto customer;
    @NotNull
    private ProductDto product;
}
