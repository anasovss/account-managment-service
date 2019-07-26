package ru.domclick.am.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.domclick.am.repository.entity.AccountEntity;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID>, JpaSpecificationExecutor<AccountEntity> {

}
