package com.egs.ATMService.common;

/**
 * ENUM type for different types of authentication methods.
 */
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
