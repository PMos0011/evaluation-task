package moskwa.com.customer.controller;

import lombok.RequiredArgsConstructor;
import moskwa.com.customer.model.CustomerDto;
import moskwa.com.customer.service.CustomerService;
import moskwa.com.customer.service.CustomerServiceFailure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(consumes = "application/json", produces = "application/json")
public class CustomerController {

    private final CustomerService customerService;
    private final Map<CustomerServiceFailure, HttpStatus> responseStatuses = Map.of(
            CustomerServiceFailure.CUSTOMER_EXISTS, HttpStatus.CONFLICT,
            CustomerServiceFailure.VALIDATION_FAILURE, HttpStatus.UNPROCESSABLE_ENTITY
    );

    @PostMapping("/create-customer")
    public ResponseEntity<Void> createCustomer(@RequestBody CustomerDto customer) {
        return customerService.createCustomer(customer)
                .map(failure -> new ResponseEntity<Void>(responseStatuses.get(failure)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CREATED));
    }

    @GetMapping("/get-customers")
    public ResponseEntity<List<CustomerDto>> getCustomers(@RequestParam(value = "ids") List<Long> creditIds) {
        return ResponseEntity.ok().body(customerService.getCustomers(creditIds));
    }
}
