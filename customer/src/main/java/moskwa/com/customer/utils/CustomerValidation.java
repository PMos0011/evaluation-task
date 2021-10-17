package moskwa.com.customer.utils;

import moskwa.com.customer.model.CustomerDto;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class CustomerValidation {

    private final static int PESEL_LENGTH = 11;

    public static boolean validateCustomer(CustomerDto customer){
        if(StringUtils.isBlank(customer.getPesel()))
            return false;

        return Objects.nonNull(customer.getCreditId())
                && StringUtils.isNotBlank(customer.getFirstName())
                && StringUtils.isNotBlank(customer.getSurname())
                && customer.getPesel().replaceAll("[^\\d]", "").length() == PESEL_LENGTH;
    }
}
