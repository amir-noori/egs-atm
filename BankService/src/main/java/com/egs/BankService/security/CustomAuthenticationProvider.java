package com.egs.BankService.security;

import com.egs.BankService.common.model.AuthenticateUserCardRequest;
import com.egs.BankService.service.impl.BankCardDetailServiceImpl;
import com.egs.BankService.util.HashUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Custom authenticator to check the bank card and ban user.
 *
 * @author Amir
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LogManager.getLogger(CustomAuthenticationProvider.class);

    private BankCardDetailServiceImpl bankCardDetailService;

    @Autowired
    public CustomAuthenticationProvider(BankCardDetailServiceImpl bankCardDetailService) {
        this.bankCardDetailService = bankCardDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AuthenticateUserCardRequest authenticateUserCardRequest = (AuthenticateUserCardRequest) authentication.getCredentials();
        bankCardDetailService.authenticateUserCard(authenticateUserCardRequest);

        switch (authenticateUserCardRequest.getAuthMethod()) {
            case PIN_CODE:
                return new UsernamePasswordAuthenticationToken(authenticateUserCardRequest.getCardNumber(), authenticateUserCardRequest.getPinCode(), new ArrayList<>());
            case FINGER_PRINT:
                return new UsernamePasswordAuthenticationToken(HashUtils.md5(authenticateUserCardRequest.getFingerPrintData()), authenticateUserCardRequest.getPinCode(), new ArrayList<>());
        }

        return null;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}