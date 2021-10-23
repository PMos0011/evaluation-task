package moskwa.com.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractBaseEntity{
    @Id
    private long creditId;
    private String productName;
    private BigDecimal value;

    public static Product fromDto(ProductDto productDto) {
        return Product.builder()
                .creditId(productDto.getCreditId())
                .productName(productDto.getProductName())
                .value(productDto.getValue())
                .build();
    }

    public ProductDto toDto() {
        return ProductDto.builder()
                .creditId(creditId)
                .productName(productName)
                .value(value)
                .build();
    }
}
