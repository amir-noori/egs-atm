package com.egs.BankService.service.impl;

import com.egs.BankService.common.AuthMethod;
import com.egs.BankService.common.model.AuthenticateUserCardRequest;
import com.egs.BankService.common.model.AuthenticateUserCardResponse;
import com.egs.BankService.data.model.BankCard;
import com.egs.BankService.data.model.BankUser;
import com.egs.BankService.data.repository.BankCardRepository;
import com.egs.BankService.data.repository.BankTransactionRepository;
import com.egs.BankService.data.repository.BankUserRepository;
import com.egs.BankService.exception.CustomAuthenticationException;
import com.egs.BankService.service.api.BankCardDetailService;
import com.egs.BankService.util.FingerPrintUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BankCardDetailServiceImpl implements BankCardDetailService {

    private static final Logger log = LogManager.getLogger(BankCardDetailServiceImpl.class);


    @Autowired
    BankCardRepository bankCardRepository;

    @Autowired
    BankUserRepository bankUserRepository;

    @Autowired
    BankTransactionRepository bankTransactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public AuthenticateUserCardResponse authenticateUserCard(AuthenticateUserCardRequest request) throws AuthenticationException {
        AuthenticateUserCardResponse response = new AuthenticateUserCardResponse();
        response.setValid(false);

        AuthMethod authMethod = request.getAuthMethod();
        if (authMethod == null) {
            response.setResponseMessage("Authentication method is not provided!");
            return response;
        }

        Optional<BankCard> byCardNumber = bankCardRepository.findOneByCardNumber(request.getCardNumber());
        if (byCardNumber.isPresent()) {
            BankCard card = byCardNumber.get();
            BankUser user = card.getBankUser();

            if (card.getBlocked()) {
                response.setResponseMessage("Card is blocked!");
            } else if (user != null && user.getId().equals(request.getUserId())) {
                switch (authMethod) {
                    case PIN_CODE:
                        if (passwordEncoder.matches(request.getPinCode(), card.getPinCode())) {
                            response.setValid(true);
                            response.setResponseMessage("Authenticate Success!");
                        } else {
                            failedAuthenticationAttempts(card);
                            throw new CustomAuthenticationException("Wrong PinCode!");
                        }
                        break;
                    case FINGER_PRINT:
                        if (FingerPrintUtil.matchFingerPrints(user.getFingerPrint(), request.getFingerPrintData())) {
                            response.setValid(true);
                            response.setResponseMessage("Authenticate Success!");
                        } else {
                            failedAuthenticationAttempts(card);
                            throw new CustomAuthenticationException("Wrong Finger Print!");
                        }
                        break;
                }
            } else {
                throw new CustomAuthenticationException("Invalid user!");
            }
        } else {
            throw new CustomAuthenticationException("No such card exists!");
        }
        return response;

    }

    private void failedAuthenticationAttempts(BankCard card) {
        Integer wrongAuthAttemptCount = card.getWrongAuthAttemptCount();
        if (wrongAuthAttemptCount == null) {
            wrongAuthAttemptCount = 0;
        }
        wrongAuthAttemptCount++;

        if (wrongAuthAttemptCount >= 3) {
            card.setBlocked(true);
        }
        bankCardRepository.save(card);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BankCard> bankCardOptional = bankCardRepository.findOneByCardNumber(Long.valueOf(username));

        if (bankCardOptional.isEmpty()) throw new UsernameNotFoundException(username);

        BankCard bankCard = bankCardOptional.get();

        return new User(
                String.valueOf(bankCard.getCardNumber()),
                bankCard.getPinCode(),
                true,
                true,
                true,
                true,
                new ArrayList<>());

    }
}
