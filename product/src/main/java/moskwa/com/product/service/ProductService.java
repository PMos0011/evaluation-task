package moskwa.com.product.service;

import lombok.RequiredArgsConstructor;
import moskwa.com.product.model.Product;
import moskwa.com.product.model.ProductDto;
import moskwa.com.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static moskwa.com.product.service.ProductServiceFailure.PRODUCT_EXISTS;
import static moskwa.com.product.service.ProductServiceFailure.VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Validator validator;

    public Optional<ProductServiceFailure> createProduct(final ProductDto productToAdd) {
        if (!validator.validate(productToAdd).isEmpty())
            return Optional.of(VALIDATION_FAILURE);

        return productRepository.findById(productToAdd.getCreditId())
                .map(product -> Optional.of(PRODUCT_EXISTS))
                .orElseGet(() -> {
                    productRepository.save(Product.fromDto(productToAdd));
                    return Optional.empty();
                });
    }

    public List<ProductDto> getProducts(List<Long> creditIds) {
        return productRepository.findAllById(creditIds)
                .stream()
                .map(Product::toDto)
                .collect(Collectors.toList());
    }
}
