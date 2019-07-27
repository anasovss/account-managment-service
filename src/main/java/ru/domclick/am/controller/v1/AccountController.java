package ru.domclick.am.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.domclick.am.account.api.AccountsApi;
import ru.domclick.am.account.dto.OperationData;
import ru.domclick.am.account.dto.OperationResponse;
import ru.domclick.am.account.dto.TransferRequest;
import ru.domclick.am.exception.BadRequestAccountException;
import ru.domclick.am.service.AccountService;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AccountController implements AccountsApi {

    private final AccountService accountService;

    @GetMapping(value = "accounts/{accountNumber}")
    public ResponseEntity get(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.findByAccountNumber(accountNumber));
    }

    @Override
    public ResponseEntity<OperationResponse> deposit(String accountNumber, OperationData operationData) {
        accountService.deposit(accountNumber, operationData.getSumRub());
        return ResponseEntity.ok(new OperationResponse().messageResult("deposit SUCCESS"));
    }

    @Override
    public ResponseEntity<OperationResponse> transfer(TransferRequest transferRequest) {
        if (transferRequest.getAccountNumberFrom().equals(transferRequest.getAccountNumberTo())) {
            throw new BadRequestAccountException("Account numbers are the same");
        }
        accountService.transfer(
                transferRequest.getAccountNumberFrom(),
                transferRequest.getAccountNumberTo(),
                transferRequest.getOperationData().getSumRub()
        );
        return ResponseEntity.ok(new OperationResponse().messageResult("transfer SUCCESS"));
    }

    @Override
    public ResponseEntity<OperationResponse> withdraw(String accountNumber, OperationData operationData) {
        accountService.withdraw(accountNumber, operationData.getSumRub());
        return ResponseEntity.ok(new OperationResponse().messageResult("withdraw SUCCESS"));
    }
}
