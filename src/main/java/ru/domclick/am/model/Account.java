package ru.domclick.am.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account")
@Getter
@Setter
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "sum_rub")
    private BigDecimal sumRub;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @PreUpdate
    protected void onUpdate() {
        modifyDate = LocalDateTime.now();
    }
}
