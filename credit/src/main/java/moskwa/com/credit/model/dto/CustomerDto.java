package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerDto {
    private final String firstName;
    private final String surname;
    private final String pesel;
}
