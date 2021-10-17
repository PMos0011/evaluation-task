package moskwa.com.customer.service;

import lombok.RequiredArgsConstructor;
import moskwa.com.customer.model.Customer;
import moskwa.com.customer.repository.CustomerRepository;
import moskwa.com.customer.utils.CustomerValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static moskwa.com.customer.service.CustomerServiceFailure.CUSTOMER_EXISTS;
import static moskwa.com.customer.service.CustomerServiceFailure.VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<CustomerServiceFailure> createCustomer(final Customer customerToAdd) {
        if (!CustomerValidation.validateCustomer(customerToAdd))
            return Optional.of(VALIDATION_FAILURE);

        return customerRepository.findById(customerToAdd.getCreditId())
                .map(customer -> Optional.of(CUSTOMER_EXISTS))
                .orElseGet(() -> {
                    customerRepository.save(customerToAdd);
                    return Optional.empty();
                });
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public void revert(final Customer customer) {
        customerRepository.delete(customer);
    }
}
