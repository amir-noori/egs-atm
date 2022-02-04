package com.egs.BankService.service.api;

import com.egs.BankService.common.model.AuthenticateUserCardRequest;
import com.egs.BankService.common.model.AuthenticateUserCardResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface BankCardDetailService extends UserDetailsService {

    public AuthenticateUserCardResponse authenticateUserCard(AuthenticateUserCardRequest request) throws AuthenticationException;


}
