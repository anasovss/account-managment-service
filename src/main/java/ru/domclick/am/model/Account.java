package ru.domclick.am.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "account")
@Getter
@Setter
@EqualsAndHashCode
public class Account {
    private UUID id;
    private UUID clientId;
    private String accountNumber;
    private BigDecimal sumRub;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
