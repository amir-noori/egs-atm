package com.egs.BankService.common.model;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    protected Integer responseCode;
    protected String responseMessage;


    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
