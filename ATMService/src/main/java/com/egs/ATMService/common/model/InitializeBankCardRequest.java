package com.egs.ATMService.common.model;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InitializeBankCardRequest extends BaseRequest {

    public InitializeBankCardRequest() {
    }

    public InitializeBankCardRequest(@NotNull(message = "Card number cannot be empty.")
                                     @NotEmpty(message = "Card number cannot be empty.") Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    @NotNull(message = "Card number cannot be empty.")
    @NotEmpty(message = "Card number cannot be empty.")
    private Long cardNumber;

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }
}
