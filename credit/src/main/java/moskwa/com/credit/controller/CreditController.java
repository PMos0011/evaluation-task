package moskwa.com.credit.controller;

import lombok.RequiredArgsConstructor;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.service.CreditService;
import moskwa.com.credit.service.CreditServiceFailure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(consumes = "application/json", produces = "application/json")
public class CreditController {

    private final CreditService creditService;
    private final Map<CreditServiceFailure, HttpStatus> responseStatuses = Map.of(
            CreditServiceFailure.VALIDATION_FAILURE, HttpStatus.UNPROCESSABLE_ENTITY,
            CreditServiceFailure.PERSIST_FAILURE, HttpStatus.FAILED_DEPENDENCY
    );

    @PostMapping("/create-credit")
    public ResponseEntity<Long> createCredit(@RequestBody CreditRequestDto creditRequestDto) {
        return creditService.createCredit(creditRequestDto)
                .fold(creditServiceFailure -> new ResponseEntity<>(responseStatuses.get(creditServiceFailure)),
                        creditId -> new ResponseEntity<>(creditId, HttpStatus.CREATED));
    }

    @GetMapping("/get-credits")
    public ResponseEntity<List<CreditRequestDto>> getCredits() {
        return creditService.getCredits()
                .map(creditRequestDtos -> new ResponseEntity<>(creditRequestDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
