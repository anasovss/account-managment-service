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

import static java.math.BigDecimal.ROUND_DOWN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    @Value("${errorResponse.insufficientFunds}")
    private String insufficientFunds;
    @Value("${errorResponse.sameAccounts}")
    private String sameAccounts;
    private final AccountRepository accountRepository;

    /**
     * Операция перевода средств с одного счета на другой.
     * Вызываем lock по сортированному порядку счетов, чтобы избежать deadlock в случае, например,
     * когда у нас 2 потока одновременно вызывают методы
     * transfer(accountNumber1, accountNumber2, sum) и transfer(accountNumber2, accountNumber1, sum) соответственно.
     *
     * @param accountNumberFrom номер счета списания
     * @param accountNumberTo   номер счета зачисления
     * @param sumRub            сумма перевода в рублях
     */
    @Transactional
    public void transfer(String accountNumberFrom, String accountNumberTo, BigDecimal sumRub) {
        if (accountNumberFrom.equals(accountNumberTo)) {
            throw new BadRequestAccountException(sameAccounts);
        }
        String firstLockAccountNumber = accountNumberFrom.compareTo(accountNumberTo) > 0 ? accountNumberFrom : accountNumberTo;
        String secondLockAccountNumber = firstLockAccountNumber.equals(accountNumberFrom) ? accountNumberTo : accountNumberFrom;
        Account firstAccount = lockByAccountNumber(firstLockAccountNumber);
        Account secondAccount = lockByAccountNumber(secondLockAccountNumber);
        Account accountTo = firstAccount.getAccountNumber().equals(accountNumberTo) ? firstAccount : secondAccount;
        Account accountFrom = firstAccount.getAccountNumber().equals(accountNumberFrom) ? firstAccount : secondAccount;
        if (accountFrom.getSumRub().compareTo(sumRub) < 0) {
            throw new BadRequestAccountException(insufficientFunds);
        }
        sumRub = sumRub.setScale(2, ROUND_DOWN);
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
        account.setSumRub(account.getSumRub().add(sumRub.setScale(2, ROUND_DOWN)));
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
        account.setSumRub(account.getSumRub().subtract(sumRub.setScale(2, ROUND_DOWN)));
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
