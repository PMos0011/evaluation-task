package moskwa.com.credit.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import moskwa.com.credit.model.Credit;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.repository.CreditRepository;
import moskwa.com.credit.rest.CustomerRESTService;
import moskwa.com.credit.rest.ProductRESTService;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.model.dto.ProductRESTDto;
import moskwa.com.credit.utils.CreditMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static moskwa.com.credit.service.CreditServiceFailure.VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final Validator validator;
    private final CreditRepository creditRepository;
    private final CustomerRESTService customerRESTService;
    private final ProductRESTService productRESTService;
    private final CreditMapper creditMapper;

    public Either<CreditServiceFailure, Long> createCredit(final CreditRequestDto requestDto) {
        if (!isRequestValid(requestDto))
            return Either.left(VALIDATION_FAILURE);

        Credit newCredit = creditRepository.save(new Credit(requestDto.getCredit().getCreditName()));

        return customerRESTService.createCustomerOrGetFailure(CustomerRESTDto.createCustomerRestDTO(newCredit.getId(), requestDto.getCustomer()))
                .<Either<CreditServiceFailure, Long>>map(creditServiceFailure -> {
                    creditRepository.delete(newCredit);
                    return Either.left(creditServiceFailure);
                }).orElseGet(() -> createProductOrGetFailure(newCredit, requestDto));
    }

    public Optional<List<CreditRequestDto>> getCredits() {
        return customerRESTService.getCustomers()
                .flatMap(customerRESTDtos -> productRESTService.getProducts()
                        .flatMap(productRESTDtos ->
                                resolveCreditRequestStos(creditRepository.findAll(), customerRESTDtos, productRESTDtos)));
    }

    private boolean isRequestValid(final CreditRequestDto requestDto) {
        return validator.validate(requestDto).isEmpty()
                && validator.validate(requestDto.getCredit()).isEmpty()
                && validator.validate(requestDto.getCustomer()).isEmpty()
                && validator.validate(requestDto.getProduct()).isEmpty();
    }

    private Either<CreditServiceFailure, Long> createProductOrGetFailure(final Credit newCredit, final CreditRequestDto requestDto) {
        return productRESTService.createProductOrGetFailure(ProductRESTDto.createRESTDto(newCredit.getId(), requestDto.getProduct()))
                .<Either<CreditServiceFailure, Long>>map(creditServiceFailure -> {
                    customerRESTService.revertCreatedCustomer(CustomerRESTDto.createCustomerRestDTO(newCredit.getId(), requestDto.getCustomer()));
                    creditRepository.delete(newCredit);
                    return Either.left(creditServiceFailure);
                }).orElseGet(() -> Either.right(newCredit.getId()));
    }

    private Optional<List<CreditRequestDto>> resolveCreditRequestStos(List<Credit> credits, List<CustomerRESTDto> customers, List<ProductRESTDto> products) {
        List<CreditRequestDto> creditRequestDtos = new ArrayList<>();
        credits.forEach(credit ->
                creditMapper.mapToCreditRequestDto(credit, customers, products).ifPresent(creditRequestDtos::add)
        );

        if (creditRequestDtos.size() == credits.size())
            return Optional.of(creditRequestDtos);

        return Optional.empty();
    }
}
