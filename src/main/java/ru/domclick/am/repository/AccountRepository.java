package ru.domclick.am.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.domclick.am.model.Account;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Account t where t.accountNumber = ?1")
    Account lockByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
}
