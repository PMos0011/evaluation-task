package moskwa.com.credit.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import moskwa.com.credit.model.Credit;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.repository.CreditRepository;
import moskwa.com.credit.rest.CustomerClient;
import moskwa.com.credit.rest.ProductClient;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.model.dto.ProductRESTDto;
import moskwa.com.credit.utils.CreditMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static moskwa.com.credit.model.dto.CustomerRESTDto.createCustomerRestDTO;
import static moskwa.com.credit.model.dto.ProductRESTDto.createProductRESTDto;
import static moskwa.com.credit.service.CreditServiceFailure.VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
public class CreditService {

    private static final int CREDIT_NUMBER_DEFAULT_INCREMENT_VALUE = 1;
    private static final long FIRST_CREDIT_NUMBER = 1L;

    private final Validator validator;
    private final CreditRepository creditRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final CreditMapper creditMapper;

    public Either<CreditServiceFailure, Long> createCredit(final CreditRequestDto requestDto) {
        if (!isRequestValid(requestDto))
            return Either.left(VALIDATION_FAILURE);

        final long creditNumber = creditRepository.getLastId()
                .map(id -> id + CREDIT_NUMBER_DEFAULT_INCREMENT_VALUE)
                .orElse(FIRST_CREDIT_NUMBER);

        return productClient.createProductOrGetFailure(createProductRESTDto(creditNumber, requestDto.getProduct()))
                .<Either<CreditServiceFailure, Long>>map(Either::left)
                .orElseGet(() -> customerClient.createCustomerOrGetFailure(createCustomerRestDTO(creditNumber, requestDto.getCustomer()))
                        .<Either<CreditServiceFailure, Long>>map(Either::left)
                        .orElseGet(() -> {
                            creditRepository.save(new Credit(creditNumber, requestDto.getCredit().getCreditName()));
                            return Either.right(creditNumber);
                        }));
    }

    public Optional<List<CreditRequestDto>> getCredits() {
        List<Credit> credits = creditRepository.findAll();
        if (credits.size() == 0)
            return Optional.of(new ArrayList<>());

        final String ids = credits.stream()
                .map(credit -> String.valueOf(credit.getId()))
                .collect(Collectors.joining(","));

        return customerClient.getCustomers(ids)
                .flatMap(customerRESTDtos -> productClient.getProducts(ids)
                        .flatMap(productRESTDtos ->
                                resolveCreditRequestDtos(credits, customerRESTDtos, productRESTDtos)));
    }

    private boolean isRequestValid(final CreditRequestDto requestDto) {
        return validator.validate(requestDto).isEmpty()
                && validator.validate(requestDto.getCredit()).isEmpty()
                && validator.validate(requestDto.getCustomer()).isEmpty()
                && validator.validate(requestDto.getProduct()).isEmpty();
    }

    private Optional<List<CreditRequestDto>> resolveCreditRequestDtos(List<Credit> credits, List<CustomerRESTDto> customers, List<ProductRESTDto> products) {
        List<CreditRequestDto> creditRequestDtos = new ArrayList<>();
        credits.forEach(credit ->
                creditMapper.mapToCreditRequestDto(credit, customers, products).ifPresent(creditRequestDtos::add)
        );

        if (creditRequestDtos.size() == credits.size())
            return Optional.of(creditRequestDtos);

        return Optional.empty();
    }
}
