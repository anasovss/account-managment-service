package ru.domclick.am.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import ru.domclick.am.model.Account;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000"), @QueryHint(name = "javax.persistence.query.timeout", value = "10000")})
    @Query("select t from Account t where t.accountNumber = ?1")
    Optional<Account> lockByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
}
