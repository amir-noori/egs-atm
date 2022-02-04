package com.egs.ATMService.common;


public enum AuthMethod {

    PIN_CODE("1"),
    FINGER_PRINT("2");

    private final String value;

    AuthMethod(String s) {
        this.value = s;
    }

    public String value() {
        return this.value;
    }
}
