package ru.domclick.am.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domclick.am.repository.AccountRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * @param AccountNumberFrom номер счета списания
     * @param AccountNumberTo номер счета зачисления
     * @param sumRub сумма перевода в рублях
     */
    @Transactional
    public void transfer(String AccountNumberFrom, String AccountNumberTo, BigDecimal sumRub) {

    }

    /**
     * @param AccountNumber номер счета внесения средств
     * @param sumRub сумма в рублях
     */
    public void deposit(String AccountNumber, BigDecimal sumRub) {

    }
}
