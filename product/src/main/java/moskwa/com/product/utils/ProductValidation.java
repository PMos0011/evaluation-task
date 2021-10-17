package moskwa.com.product.utils;


import moskwa.com.product.model.ProductDto;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ProductValidation {

    public static boolean validateProduct(ProductDto product) {
        return Objects.nonNull(product.getCreditId())
                && StringUtils.isNotBlank(product.getProductName())
                && Objects.nonNull(product.getValue());
    }
}
