package com.gyarsilalsolanki011.banking.entity;

import com.gyarsilalsolanki011.banking.enums.TransactionStatus;
import com.gyarsilalsolanki011.banking.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Date date = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    public Transaction(Account account, Double amount, TransactionType transactionType, TransactionStatus status) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}
