package moskwa.com.credit.constroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import moskwa.com.credit.BaseIntegrationTest;
import moskwa.com.credit.annotations.CreditAfter;
import moskwa.com.credit.model.dto.CreditRequestDto;
import moskwa.com.credit.repository.CreditRepository;
import moskwa.com.credit.rest.TestCustomerRESTService;
import moskwa.com.credit.rest.TestProductRESTService;
import moskwa.com.credit.CreditRequestDtoBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static moskwa.com.credit.CreditRequestDtoBuilder.createCreditRequestCreator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CreditControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestCustomerRESTService customerClient;

    @Autowired
    private TestProductRESTService productClient;

    @Autowired
    private CreditRepository creditRepository;

    @Test
    @CreditAfter
    public void shouldReturnCreditNumber() throws Exception {
        CreditRequestDto creditRequestDto = createCreditRequestCreator(1);

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json("1"));
    }

    @Test
    @CreditAfter
    public void shouldPersistThreeCreditsAndReturnThreeCredits() throws Exception {
        CreditRequestDto creditRequestDtoOne = createCreditRequestCreator(1);
        CreditRequestDto creditRequestDtoTwo = createCreditRequestCreator(2);
        CreditRequestDto creditRequestDtoThree = createCreditRequestCreator(3);

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDtoOne)))
                .andExpect(status().isCreated())
                .andExpect(content().json("1"));

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDtoTwo)))
                .andExpect(status().isCreated())
                .andExpect(content().json("2"));

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDtoThree)))
                .andExpect(status().isCreated())
                .andExpect(content().json("3"));

        String response = mockMvc.perform(get("/get-credits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CreditRequestDto[] creditRequestDtos = objectMapper.readValue(response, CreditRequestDto[].class);
        for (CreditRequestDto creditRequestDto : creditRequestDtos) {
            assertThat(creditRequestDto.getCredit().getCreditName()).isEqualTo(creditRequestDto.getCustomer().getSurname());
            assertThat(creditRequestDto.getCredit().getCreditName()).isEqualTo(creditRequestDto.getProduct().getProductName());
        }
    }

    @Test
    public void shouldReturnUnprocessableWhenCreditNameMissing() throws Exception {
        CreditRequestDto creditRequestDto = CreditRequestDtoBuilder.createWithoutCreditName();

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnUnprocessableWhenProductIsMissing() throws Exception {
        CreditRequestDto creditRequestDto = CreditRequestDtoBuilder.createWithoutProduct();

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnUnprocessableWhenCustomerIsMissing() throws Exception {
        CreditRequestDto creditRequestDto = CreditRequestDtoBuilder.createWithoutCustomer();

        mockMvc.perform(post("/create-credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditRequestDto)))
                .andExpect(status().isUnprocessableEntity());
    }
}
