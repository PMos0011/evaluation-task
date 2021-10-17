package moskwa.com.product.controller;

import lombok.RequiredArgsConstructor;
import moskwa.com.product.model.ProductDto;
import moskwa.com.product.service.ProductService;
import moskwa.com.product.service.ProductServiceFailure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(consumes = "application/json", produces = "application/json")
public class ProductController {

    private final ProductService productService;
    private final Map<ProductServiceFailure, HttpStatus> responseStatuses = Map.of(
            ProductServiceFailure.PRODUCT_EXISTS, HttpStatus.CONFLICT,
            ProductServiceFailure.VALIDATION_FAILURE, HttpStatus.UNPROCESSABLE_ENTITY
    );

    @PostMapping("/create-product")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDto product) {
        return productService.createProduct(product)
                .map(failure -> new ResponseEntity<Void>(responseStatuses.get(failure)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CREATED));
    }

    @GetMapping("/get-products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok().body(productService.getProducts());
    }
}
