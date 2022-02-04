package com.egs.ATMService.common.model;

public class InitializeBankCardResponse extends BaseResponse {

    private Boolean isValid;

    public InitializeBankCardResponse() {
    }

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
