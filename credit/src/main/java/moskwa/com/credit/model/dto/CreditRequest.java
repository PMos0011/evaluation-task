package moskwa.com.credit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreditRequest {
    private CreditDto credit;
    private CustomerDto customer;
    private ProductDto product;
}
