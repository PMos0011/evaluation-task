package moskwa.com.credit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String surname;
    @NotNull
    @Pattern(regexp="[\\d]{11}")
    private String pesel;
}
