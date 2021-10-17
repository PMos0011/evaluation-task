package moskwa.com.customer.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerDto {
    private final Long creditId;
    private final String firstName;
    private final String surname;
    private final String pesel;
}
