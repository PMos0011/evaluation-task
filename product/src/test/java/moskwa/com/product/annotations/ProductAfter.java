package moskwa.com.product.annotations;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SqlGroup(
        @Sql(scripts = "/test-sql/clear-product.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
                config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ProductAfter {
}
