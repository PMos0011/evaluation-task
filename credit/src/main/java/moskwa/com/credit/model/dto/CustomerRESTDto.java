package moskwa.com.credit.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerRESTDto {
    private final long creditId;
    private final String firstName;
    private final String surname;
    private final String pesel;

    public static CustomerRESTDto createCustomerRestDTO(final long creditId, final CustomerDto customerDto){
        return CustomerRESTDto.builder()
                .creditId(creditId)
                .firstName(customerDto.getFirstName())
                .surname(customerDto.getSurname())
                .pesel(customerDto.getPesel())
                .build();
    }

    public CustomerDto toDto(){
        return CustomerDto.builder()
                .firstName(firstName)
                .surname(surname)
                .pesel(pesel)
                .build();
    }
}
