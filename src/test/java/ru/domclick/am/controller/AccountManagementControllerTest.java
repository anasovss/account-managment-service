package ru.domclick.am.controller;

import com.google.gson.Gson;
import liquibase.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.domclick.am.account.dto.OperationData;
import ru.domclick.am.account.dto.TransferRequest;
import ru.domclick.am.service.AccountService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountManagementControllerTest {

    @Autowired
    private MockMvc mvc;
    private final static String ACCOUNT_NUMBER_1 = "11112222333344445555";
    private final static String ACCOUNT_NUMBER_2 = "11112222333344445556";

    @Autowired
    private AccountService accountService;

    @Test
    void transfer200OkTest() throws Exception {
        BigDecimal account1Sum = accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub();
        BigDecimal account2Sum = accountService.findByAccountNumber(ACCOUNT_NUMBER_2).getSumRub();
        mvc.perform(post("/v1/accounts/transfer")
                .content(new Gson().toJson(transferRequest(new BigDecimal("200"))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertThat(accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub()).isEqualTo(account1Sum.subtract(new BigDecimal("200")));
        assertThat(accountService.findByAccountNumber(ACCOUNT_NUMBER_2).getSumRub()).isEqualTo(account2Sum.add(new BigDecimal("200")));
    }

    @Test
    void transfer400InsufficientFundsTest() throws Exception {
        BigDecimal account1Sum = accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub();
        mvc.perform(post("/v1/accounts/transfer")
                .content(new Gson().toJson(transferRequest(account1Sum.add(BigDecimal.ONE))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void transfer400LessThanAvailableMinSumTest() throws Exception {
        mvc.perform(post("/v1/accounts/transfer")
                .content(new Gson().toJson(transferRequest(new BigDecimal("0.5"))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void withdraw200OkTest() throws Exception {
        BigDecimal sumRub = accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub();
        mvc.perform(post("/v1/accounts/{accountNumber}/withdraw", ACCOUNT_NUMBER_1)
                .content(new Gson().toJson(new OperationData().sumRub(sumRub)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertThat(accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void withdraw404NotFoundTest() throws Exception {
        mvc.perform(post("/v1/accounts/{accountNumber}/withdraw", StringUtils.repeat("21",10))
                .content(new Gson().toJson(new OperationData().sumRub(BigDecimal.TEN)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void withdraw400InsufficientFundsTest() throws Exception {
        BigDecimal sumRub = accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub();
        mvc.perform(post("/v1/accounts/{accountNumber}/withdraw", ACCOUNT_NUMBER_1)
                .content(new Gson().toJson(new OperationData().sumRub(sumRub.add(BigDecimal.ONE))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void deposit200OkTest() throws Exception {
        BigDecimal sumRub = accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub();
        mvc.perform(post("/v1/accounts/{accountNumber}/deposit", ACCOUNT_NUMBER_1)
                .content(new Gson().toJson(new OperationData().sumRub(sumRub)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertThat(accountService.findByAccountNumber(ACCOUNT_NUMBER_1).getSumRub()).isEqualTo(sumRub.add(sumRub));
    }

    @Test
    void deposit404NotFoundTest() throws Exception {
        mvc.perform(post("/v1/accounts/{accountNumber}/deposit", StringUtils.repeat("21",10))
                .content(new Gson().toJson(new OperationData().sumRub(BigDecimal.ONE)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private TransferRequest transferRequest(BigDecimal sum) {
        return new TransferRequest()
                .accountNumberFrom(ACCOUNT_NUMBER_1)
                .accountNumberTo(ACCOUNT_NUMBER_2)
                .operationData(new OperationData().sumRub(sum));
    }
}
