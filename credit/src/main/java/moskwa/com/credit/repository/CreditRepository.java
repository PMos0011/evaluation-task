package moskwa.com.credit.repository;

import moskwa.com.credit.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    @Query("select max(id) from Credit")
    Optional<Long> getLastId();
}
