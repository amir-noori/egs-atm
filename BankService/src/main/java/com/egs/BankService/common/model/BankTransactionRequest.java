package com.egs.BankService.common.model;


public class BankTransactionRequest extends BaseRequest {

    private long amount;

    public BankTransactionRequest(long cardNumber, long amount) {
        this.cardNumber = cardNumber;
        this.amount = amount;
    }

    public BankTransactionRequest() {
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
