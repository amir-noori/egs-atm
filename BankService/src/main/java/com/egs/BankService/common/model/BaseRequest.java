package com.egs.BankService.common.model;

import java.io.Serializable;

public class BaseRequest implements Serializable {

    protected long cardNumber;

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }
}
