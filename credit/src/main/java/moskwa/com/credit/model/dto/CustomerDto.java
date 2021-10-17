package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class CustomerDto {
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String surname;
    @NotNull
    @Pattern(regexp="[\\d]{11}")
    private final String pesel;
}
