package moskwa.com.product.service;

import lombok.RequiredArgsConstructor;
import moskwa.com.product.model.Product;
import moskwa.com.product.repository.ProductRepository;
import moskwa.com.product.utils.ProductValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static moskwa.com.product.service.ProductServiceFailure.PRODUCT_EXISTS;
import static moskwa.com.product.service.ProductServiceFailure.VALIDATION_FAILURE;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Optional<ProductServiceFailure> createProduct(final Product productToAdd) {
        if (!ProductValidation.validateProduct(productToAdd))
            return Optional.of(VALIDATION_FAILURE);

        return productRepository.findById(productToAdd.getCreditId())
                .map(product -> Optional.of(PRODUCT_EXISTS))
                .orElseGet(() -> {
                    productRepository.save(productToAdd);
                    return Optional.empty();
                });
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }
}
