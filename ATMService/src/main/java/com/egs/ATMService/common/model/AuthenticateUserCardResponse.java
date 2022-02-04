package com.egs.ATMService.common.model;

public class AuthenticateUserCardResponse extends BaseResponse {

    private Boolean isValid;

//    public AuthenticateUserCardResponse(Boolean isValid) {
//        super();
//        this.isValid = isValid;
//    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

}
