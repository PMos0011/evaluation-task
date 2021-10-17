package moskwa.com.credit.rest;

import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.service.CreditServiceFailure;

import java.util.List;
import java.util.Optional;

public interface CustomerClient {
    Optional<CreditServiceFailure> createCustomerOrGetFailure(CustomerRESTDto customerRESTDto);

   Optional<List<CustomerRESTDto>> getCustomers();

    void revertCreatedCustomer(CustomerRESTDto customerRESTDto);
}
