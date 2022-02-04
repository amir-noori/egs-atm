package com.egs.BankService.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown when there is an issue with JWT token for authentication.
 *
 * @author Amir
 */
public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
