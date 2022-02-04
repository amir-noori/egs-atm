package com.egs.BankService.service.api;

import com.egs.BankService.common.model.AuthenticateUserCardRequest;
import com.egs.BankService.common.model.AuthenticateUserCardResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Bank card detail services used in spring security.
 *
 * @author Amir
 */
@Service
public interface BankCardDetailService extends UserDetailsService {

    /**
     * authenticate bank card.
     *
     * @param request
     * @return
     * @throws AuthenticationException when there an authentication issue.
     */
    public AuthenticateUserCardResponse authenticateUserCard(AuthenticateUserCardRequest request) throws AuthenticationException;


}
