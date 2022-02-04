package com.egs.BankService.common.model;


public class InitializeBankCardRequest extends BaseRequest {

    public InitializeBankCardRequest(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public InitializeBankCardRequest() {
    }

}
