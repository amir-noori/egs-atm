package com.egs.ATMService.common.model;


public class BankTransactionRequest extends BaseRequest {


    private long cardNumber;
    private long amount;

    public BankTransactionRequest(long cardNumber, long amount) {
        this.cardNumber = cardNumber;
        this.amount = amount;
    }

    public BankTransactionRequest() {
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
