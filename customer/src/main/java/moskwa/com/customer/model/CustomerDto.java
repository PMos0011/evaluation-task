package moskwa.com.customer.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
public class CustomerDto {
    @NotNull
    private final Long creditId;
    @NotEmpty
    private final String firstName;
    @NotEmpty
    private final String surname;
    @Pattern(regexp="[\\d]{11}")
    private final String pesel;
}
