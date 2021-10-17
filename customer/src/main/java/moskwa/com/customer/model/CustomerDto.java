package moskwa.com.customer.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
public class CustomerDto {
    @NotNull
    private final Long creditId;
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String surname;
    @NotNull
    @Pattern(regexp = "[\\d]{11}")
    private final String pesel;
}
