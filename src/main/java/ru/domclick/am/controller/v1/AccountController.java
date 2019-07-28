package ru.domclick.am.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.domclick.am.generated.api.AccountsApi;
import ru.domclick.am.generated.dto.OperationData;
import ru.domclick.am.generated.dto.OperationResponse;
import ru.domclick.am.generated.dto.TransferRequest;
import ru.domclick.am.service.AccountService;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AccountController implements AccountsApi {

    @Value("${operationResponse.depositSuccessful}")
    private String depositSuccessfulMessage;
    @Value("${operationResponse.withdrawalSuccessful}")
    private String withdrawalSuccessfulMessage;
    @Value("${operationResponse.transferSuccessful}")
    private String transferSuccessfulMessage;
    private final AccountService accountService;

    @GetMapping(value = "accounts/{accountNumber}")
    public ResponseEntity get(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.findByAccountNumber(accountNumber));
    }

    @Override
    public ResponseEntity<OperationResponse> deposit(String accountNumber, OperationData operationData) {
        log.info("Deposit {} rubles to the account: {}", operationData.getSumRub(), accountNumber);
        accountService.deposit(accountNumber, operationData.getSumRub());
        return ResponseEntity.ok(new OperationResponse().messageResult(depositSuccessfulMessage));
    }

    @Override
    public ResponseEntity<OperationResponse> transfer(TransferRequest transferRequest) {
        log.info(
                "Transfer {} rubles from account: {} to  account: {}",
                transferRequest.getOperationData().getSumRub(),
                transferRequest.getAccountNumberFrom(),
                transferRequest.getAccountNumberTo()
        );
        accountService.transfer(
                transferRequest.getAccountNumberFrom(),
                transferRequest.getAccountNumberTo(),
                transferRequest.getOperationData().getSumRub()
        );
        return ResponseEntity.ok(new OperationResponse().messageResult(transferSuccessfulMessage));
    }

    @Override
    public ResponseEntity<OperationResponse> withdraw(String accountNumber, OperationData operationData) {
        log.info("Withdraw {} rubles from the account: {}", operationData.getSumRub(), accountNumber);
        accountService.withdraw(accountNumber, operationData.getSumRub());
        return ResponseEntity.ok(new OperationResponse().messageResult(withdrawalSuccessfulMessage));
    }
}
