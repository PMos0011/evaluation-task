package moskwa.com.product.utils;


import moskwa.com.product.model.Product;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ProductValidation {

    public static boolean validateProduct(Product product) {
        return Objects.nonNull(product.getCreditId())
                && StringUtils.isNotBlank(product.getProductName())
                && Objects.nonNull(product.getValue());
    }
}
