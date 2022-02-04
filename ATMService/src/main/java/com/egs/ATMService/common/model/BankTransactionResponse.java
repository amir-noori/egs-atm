package com.egs.ATMService.common.model;

import com.egs.ATMService.common.TransactionType;

public class BankTransactionResponse extends BaseResponse {

    private Boolean isSuccessful;
    private long cardBalance;
    private TransactionType transactionType;

    public BankTransactionResponse() {
    }

    public BankTransactionResponse(Boolean isSuccessful, long cardBalance, TransactionType transactionType) {
        this.isSuccessful = isSuccessful;
        this.cardBalance = cardBalance;
        this.transactionType = transactionType;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public long getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(long cardBalance) {
        this.cardBalance = cardBalance;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

}
