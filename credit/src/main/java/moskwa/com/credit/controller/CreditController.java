package moskwa.com.credit.controller;

import io.swagger.annotations.*;
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
@Api(value = "Controller for create new credit and receive credits data ")
@RequestMapping(consumes = "application/json", produces = "application/json")
public class CreditController {

    private final CreditService creditService;
    private final Map<CreditServiceFailure, HttpStatus> responseStatuses = Map.of(
            CreditServiceFailure.VALIDATION_FAILURE, HttpStatus.UNPROCESSABLE_ENTITY,
            CreditServiceFailure.PERSIST_FAILURE, HttpStatus.FAILED_DEPENDENCY
    );

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Creates new credit")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Credit created", response = Long.class),
            @ApiResponse(code = 422, message = "Request object invalid"),
            @ApiResponse(code = 424, message = "Error while persisting data"),
    })
    @PostMapping("/create-credit")
    public ResponseEntity<Long> createCredit(@ApiParam(value = "Request credit object. All fields are required")
                                             @RequestBody CreditRequestDto creditRequestDto) {
        return creditService.createCredit(creditRequestDto)
                .fold(creditServiceFailure -> new ResponseEntity<>(responseStatuses.get(creditServiceFailure)),
                        creditId -> new ResponseEntity<>(creditId, HttpStatus.CREATED));
    }

    @ApiOperation(value = "Returns all available credits data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Credits found", response = CreditRequestDto.class),
            @ApiResponse(code = 424, message = "Error while reading data"),
    })
    @GetMapping("/get-credits")
    public ResponseEntity<List<CreditRequestDto>> getCredits() {
        return creditService.getCredits()
                .map(creditRequestDtos -> new ResponseEntity<>(creditRequestDtos, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY));
    }
}
