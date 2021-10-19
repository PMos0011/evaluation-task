package moskwa.com.credit.rest;

import moskwa.com.credit.model.dto.ProductRESTDto;
import moskwa.com.credit.service.CreditServiceFailure;

import java.util.List;
import java.util.Optional;

public interface ProductClient {
    Optional<CreditServiceFailure> createProductOrGetFailure(ProductRESTDto productRESTDto);

    Optional<List<ProductRESTDto>> getProducts(String ids);
}
