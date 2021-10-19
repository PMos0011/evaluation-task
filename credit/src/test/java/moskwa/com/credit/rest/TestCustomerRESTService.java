package moskwa.com.credit.rest;

import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.service.CreditServiceFailure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestCustomerRESTService implements CustomerClient {
    private final List<CustomerRESTDto> customers = new ArrayList<>();

    @Override
    public Optional<CreditServiceFailure> createCustomerOrGetFailure(CustomerRESTDto customerRESTDto) {
        customers.add(customerRESTDto);
        return Optional.empty();
    }

    @Override
    public Optional<List<CustomerRESTDto>> getCustomers(String ids) {
        return Optional.of(customers);
    }
}
