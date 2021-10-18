package moskwa.com.credit.service;

import io.vavr.control.Either;
import moskwa.com.credit.BaseIntegrationTest;
import moskwa.com.credit.CreditRequestDtoBuilder;
import moskwa.com.credit.annotations.CreditAfter;
import moskwa.com.credit.annotations.CreditInit;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.model.dto.CustomerRESTDto;
import moskwa.com.credit.model.dto.ProductRESTDto;
import moskwa.com.credit.repository.CreditRepository;
import moskwa.com.credit.rest.CustomerClient;
import moskwa.com.credit.rest.ProductClient;
import moskwa.com.credit.utils.CreditMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreditServiceIT extends BaseIntegrationTest {

    @Autowired
    private Validator validator;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditMapper creditMapper;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private ProductClient productClient;

    private CreditService creditService;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
        creditService = new CreditService(validator, creditRepository, customerClient, productClient, creditMapper);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    @CreditAfter
    public void shouldReturnCreditNumber() {
        CreditRequestDto creditRequestDto = CreditRequestDtoBuilder.createCreditRequestCreator(1);

        when(customerClient.createCustomerOrGetFailure(any(CustomerRESTDto.class))).thenReturn(Optional.empty());
        when(productClient.createProductOrGetFailure(any(ProductRESTDto.class))).thenReturn(Optional.empty());

        Either<CreditServiceFailure, Long> result = creditService.createCredit(creditRequestDto);

        assertEquals(1L, result.get());
        assertEquals(1L, creditRepository.findAll().size());
    }

    @Test
    @CreditInit
    @CreditAfter
    public void shouldReturnCredits() {
        List<CustomerRESTDto> customersFromService = Stream.of(
                CustomerRESTDto.createCustomerRestDTO(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getCustomer())
        ).collect(Collectors.toList());

        List<ProductRESTDto> productsFromCService = Stream.of(
                ProductRESTDto.createRESTDto(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getProduct()),
                ProductRESTDto.createRESTDto(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getProduct()),
                ProductRESTDto.createRESTDto(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getProduct())
        ).collect(Collectors.toList());

        when(customerClient.getCustomers()).thenReturn(Optional.of(customersFromService));
        when(productClient.getProducts()).thenReturn(Optional.of(productsFromCService));

        Optional<List<CreditRequestDto>> result = creditService.getCredits();

        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());

        for (CreditRequestDto creditRequestDto : result.get()) {
            assertThat(creditRequestDto.getCredit().getCreditName()).isEqualTo(creditRequestDto.getCustomer().getSurname());
            assertThat(creditRequestDto.getCredit().getCreditName()).isEqualTo(creditRequestDto.getProduct().getProductName());
        }
    }

    @Test
    @CreditAfter
    public void shouldNotPersistCreditOnCustomerClientFailureAndReturnPersistFailure() {
        CreditRequestDto creditRequestDto = CreditRequestDtoBuilder.createCreditRequestCreator(1);

        when(customerClient.createCustomerOrGetFailure(any(CustomerRESTDto.class)))
                .thenReturn(Optional.of(CreditServiceFailure.PERSIST_FAILURE));
        when(productClient.createProductOrGetFailure(any(ProductRESTDto.class))).thenReturn(Optional.empty());

        Either<CreditServiceFailure, Long> result = creditService.createCredit(creditRequestDto);
        assertTrue(result.isLeft());
        assertEquals(CreditServiceFailure.PERSIST_FAILURE, result.getLeft());
        assertEquals(0, creditRepository.findAll().size());
    }

    @Test
    @CreditAfter
    public void shouldNotPersistCreditOnProductClientFailureAndReturnPersistFailure() {
        CreditRequestDto creditRequestDto = CreditRequestDtoBuilder.createCreditRequestCreator(1);

        when(customerClient.createCustomerOrGetFailure(any(CustomerRESTDto.class))).thenReturn(Optional.empty());
        when(productClient.createProductOrGetFailure(any(ProductRESTDto.class))).thenReturn(Optional.of(CreditServiceFailure.PERSIST_FAILURE));

        Either<CreditServiceFailure, Long> result = creditService.createCredit(creditRequestDto);

        assertTrue(result.isLeft());
        assertEquals(CreditServiceFailure.PERSIST_FAILURE, result.getLeft());
        assertEquals(0, creditRepository.findAll().size());
    }

    @Test
    @CreditInit
    @CreditAfter
    public void shouldReturnOptionalEmptyWhenMissingCustomerData(){
        List<CustomerRESTDto> customersFromService = Stream.of(
                CustomerRESTDto.createCustomerRestDTO(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getCustomer())
        ).collect(Collectors.toList());

        List<ProductRESTDto> productsFromCService = Stream.of(
                ProductRESTDto.createRESTDto(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getProduct()),
                ProductRESTDto.createRESTDto(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getProduct()),
                ProductRESTDto.createRESTDto(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getProduct())
        ).collect(Collectors.toList());

        when(customerClient.getCustomers()).thenReturn(Optional.of(customersFromService));
        when(productClient.getProducts()).thenReturn(Optional.of(productsFromCService));

        Optional<List<CreditRequestDto>> result = creditService.getCredits();

        assertTrue(result.isEmpty());
    }

    @Test
    @CreditInit
    @CreditAfter
    public void shouldReturnOptionalEmptyWhenMissingProductData(){
        List<CustomerRESTDto> customersFromService = Stream.of(
                CustomerRESTDto.createCustomerRestDTO(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getCustomer())
        ).collect(Collectors.toList());

        List<ProductRESTDto> productsFromCService = Stream.of(
                ProductRESTDto.createRESTDto(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getProduct()),
                ProductRESTDto.createRESTDto(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getProduct())
        ).collect(Collectors.toList());

        when(customerClient.getCustomers()).thenReturn(Optional.of(customersFromService));
        when(productClient.getProducts()).thenReturn(Optional.of(productsFromCService));

        Optional<List<CreditRequestDto>> result = creditService.getCredits();

        assertTrue(result.isEmpty());
    }

    @Test
    @CreditInit
    @CreditAfter
    public void shouldReturnOptionalEmptyOnCustomerClientFailure(){
        List<ProductRESTDto> productsFromCService = Stream.of(
                ProductRESTDto.createRESTDto(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getProduct()),
                ProductRESTDto.createRESTDto(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getProduct()),
                ProductRESTDto.createRESTDto(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getProduct())
        ).collect(Collectors.toList());

        when(customerClient.getCustomers()).thenReturn(Optional.empty());
        when(productClient.getProducts()).thenReturn(Optional.of(productsFromCService));

        Optional<List<CreditRequestDto>> result = creditService.getCredits();

        assertTrue(result.isEmpty());
    }

    @Test
    @CreditInit
    @CreditAfter
    public void shouldReturnOptionalEmptyOnProductClientFailure(){
        List<CustomerRESTDto> customersFromService = Stream.of(
                CustomerRESTDto.createCustomerRestDTO(1L, CreditRequestDtoBuilder.createCreditRequestCreator(1).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(2L, CreditRequestDtoBuilder.createCreditRequestCreator(2).getCustomer()),
                CustomerRESTDto.createCustomerRestDTO(3L, CreditRequestDtoBuilder.createCreditRequestCreator(3).getCustomer())
        ).collect(Collectors.toList());

        when(customerClient.getCustomers()).thenReturn(Optional.of(customersFromService));
        when(productClient.getProducts()).thenReturn(Optional.empty());

        Optional<List<CreditRequestDto>> result = creditService.getCredits();

        assertTrue(result.isEmpty());
    }
}
