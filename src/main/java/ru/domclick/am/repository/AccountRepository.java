package ru.domclick.am.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.domclick.am.model.Account;

import javax.persistence.LockModeType;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Account t where t.id = ?1")
    Account lockById(UUID id);
}
