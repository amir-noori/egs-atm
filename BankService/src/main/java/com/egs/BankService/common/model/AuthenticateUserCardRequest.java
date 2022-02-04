package com.egs.BankService.common.model;


import com.egs.BankService.common.AuthMethod;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AuthenticateUserCardRequest extends BaseRequest {


    @NotNull(message="User ID cannot be empty.")
    private Long userId;

    private String pinCode;

    private String fingerPrintData;

    @NotNull(message="Authentication method cannot be empty.")
    @NotEmpty(message="Authentication method cannot be empty.")
    private AuthMethod authMethod;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getFingerPrintData() {
        return fingerPrintData;
    }

    public void setFingerPrintData(String fingerPrintData) {
        this.fingerPrintData = fingerPrintData;
    }

    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(AuthMethod authMethod) {
        this.authMethod = authMethod;
    }
}
