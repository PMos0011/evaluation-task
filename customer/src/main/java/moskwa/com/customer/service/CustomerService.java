package moskwa.com.customer.service;

import lombok.RequiredArgsConstructor;
import moskwa.com.customer.model.Customer;
import moskwa.com.customer.model.CustomerDto;
import moskwa.com.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static moskwa.com.customer.service.CustomerServiceFailure.CUSTOMER_EXISTS;
import static moskwa.com.customer.service.CustomerServiceFailure.VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final Validator validator;

    public Optional<CustomerServiceFailure> createCustomer(final CustomerDto customerToAdd) {
        if (!validator.validate(customerToAdd).isEmpty())
            return Optional.of(VALIDATION_FAILURE);

        return customerRepository.findById(customerToAdd.getCreditId())
                .map(customer -> Optional.of(CUSTOMER_EXISTS))
                .orElseGet(() -> {
                    customerRepository.save(Customer.fromDto(customerToAdd));
                    return Optional.empty();
                });
    }

    public List<CustomerDto> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(Customer::toDto)
                .collect(Collectors.toList());
    }

    public void revert(final CustomerDto customer) {
        customerRepository.delete(Customer.fromDto(customer));
    }
}
