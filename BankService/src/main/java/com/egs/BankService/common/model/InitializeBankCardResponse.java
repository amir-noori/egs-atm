package com.egs.BankService.common.model;

public class InitializeBankCardResponse extends BaseResponse {

    public InitializeBankCardResponse() {
    }

    private Boolean isValid;

    public InitializeBankCardResponse(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

}
