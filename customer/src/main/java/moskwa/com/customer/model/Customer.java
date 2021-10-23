package moskwa.com.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractBaseEntity {
    @Id
    private long creditId;
    private String firstName;
    private String surname;
    private String pesel;

    public static Customer fromDto(CustomerDto customerDto){
        return Customer.builder()
                .creditId(customerDto.getCreditId())
                .firstName(customerDto.getFirstName())
                .surname(customerDto.getSurname())
                .pesel(customerDto.getPesel())
                .build();
    }

    public CustomerDto toDto(){
        return CustomerDto.builder()
                .creditId(creditId)
                .firstName(firstName)
                .surname(surname)
                .pesel(pesel)
                .build();
    }
}
