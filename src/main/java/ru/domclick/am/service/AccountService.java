package ru.domclick.am.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domclick.am.exception.BadRequestAccountException;
import ru.domclick.am.exception.NotFoundAccountException;
import ru.domclick.am.model.Account;
import ru.domclick.am.repository.AccountRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    @Value("${errorResponse.insufficientFunds}")
    private String insufficientFunds;
    private final AccountRepository accountRepository;

    /**
     * Операция перевода средств с одного счета на другой. Задан порядок lock, чтобы избежать deadlock
     * @param accountNumberFrom номер счета списания
     * @param accountNumberTo   номер счета зачисления
     * @param sumRub            сумма перевода в рублях
     */
    @Transactional
    public void transfer(String accountNumberFrom, String accountNumberTo, BigDecimal sumRub) {
        String firstLockAccountNumber = accountNumberFrom.compareTo(accountNumberTo) > 0 ? accountNumberFrom : accountNumberTo;
        String secondLockAccountNumber = firstLockAccountNumber.equals(accountNumberFrom) ? accountNumberTo : accountNumberFrom;
        Account firstAccount = accountRepository.lockByAccountNumber(firstLockAccountNumber).orElseThrow(() -> new NotFoundAccountException(firstLockAccountNumber));
        Account secondAccount = accountRepository.lockByAccountNumber(secondLockAccountNumber).orElseThrow(() -> new NotFoundAccountException(firstLockAccountNumber));
        Account accountTo = firstAccount.getAccountNumber().equals(accountNumberTo) ? firstAccount : secondAccount;
        Account accountFrom = firstAccount.getAccountNumber().equals(accountNumberFrom) ? firstAccount : secondAccount;
        if (accountFrom.getSumRub().compareTo(sumRub) < 0) {
            throw new BadRequestAccountException(insufficientFunds);
        }
        accountFrom.setSumRub(accountFrom.getSumRub().subtract(sumRub));
        accountTo.setSumRub(accountTo.getSumRub().add(sumRub));
    }

    /**
     * @param accountNumber номер счета внесения средств
     * @param sumRub        сумма зачисления в рублях
     */
    @Transactional
    public void deposit(String accountNumber, BigDecimal sumRub) {
        Account account = lockByAccountNumber(accountNumber);
        account.setSumRub(account.getSumRub().add(sumRub));
    }

    /**
     * @param accountNumber номер счета внесения средств
     * @param sumRub        сумма списания в рублях
     */
    @Transactional
    public void withdraw(String accountNumber, BigDecimal sumRub) {
        Account account = lockByAccountNumber(accountNumber);
        if (account.getSumRub().compareTo(sumRub) < 0) {
            throw new BadRequestAccountException(insufficientFunds);
        }
        account.setSumRub(account.getSumRub().subtract(sumRub));
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundAccountException(accountNumber));
    }

    private Account lockByAccountNumber(String accountNumber) {
        return accountRepository.lockByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundAccountException(accountNumber));
    }

}
