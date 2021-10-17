package moskwa.com.credit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerDto {
    private String firstName;
    private String surname;
    private String pesel;
}
