package moskwa.com.credit.rest;

import moskwa.com.credit.model.dto.ProductRESTDto;
import moskwa.com.credit.service.CreditServiceFailure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestProductRESTService implements ProductClient {
    private final List<ProductRESTDto> productRESTDtos = new ArrayList<>();

    @Override
    public Optional<CreditServiceFailure> createProductOrGetFailure(ProductRESTDto productRESTDto) {
        productRESTDtos.add(productRESTDto);
        return Optional.empty();
    }

    @Override
    public Optional<List<ProductRESTDto>> getProducts() {
        return Optional.of(productRESTDtos);
    }
}
