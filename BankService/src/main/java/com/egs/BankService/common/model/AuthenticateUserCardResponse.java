package com.egs.BankService.common.model;

public class AuthenticateUserCardResponse extends BaseResponse {

    private Boolean isValid;


    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

}
