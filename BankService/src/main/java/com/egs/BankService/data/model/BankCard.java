package com.egs.BankService.data.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "TBL_BANK_CARD")
public class BankCard implements Serializable {


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private BankUser bankUser;

    @OneToMany(mappedBy = "bankCard")
    private Set<BankTransaction> bankTransactions;

    @Column(nullable = false)
    private long cardNumber;

    @Column
    private long balanceAmount;

    @Column
    private Boolean isBlocked;

    @Column
    private Integer wrongAuthAttemptCount;

    @Column
    private String pinCode;

    @Column
    private Integer preferredAuthMethod;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankUser getBankUser() {
        return bankUser;
    }

    public void setBankUser(BankUser bankUser) {
        this.bankUser = bankUser;
    }

    public Integer getWrongAuthAttemptCount() {
        return wrongAuthAttemptCount;
    }

    public void setWrongAuthAttemptCount(Integer wrongAuthAttemptCount) {
        this.wrongAuthAttemptCount = wrongAuthAttemptCount;
    }

    public Set<BankTransaction> getBankTransactions() {
        return bankTransactions;
    }

    public void setBankTransactions(Set<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public long getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(long deposit) {
        this.balanceAmount = deposit;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getPreferredAuthMethod() {
        return preferredAuthMethod;
    }

    public void setPreferredAuthMethod(Integer preferredAuthMethod) {
        this.preferredAuthMethod = preferredAuthMethod;
    }
}