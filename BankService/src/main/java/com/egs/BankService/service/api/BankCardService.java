package com.egs.BankService.service.api;

import com.egs.BankService.data.model.BankCard;
import org.springframework.stereotype.Service;

/**
 * Bank Card services
 *
 * @author Amir
 */
@Service
public interface BankCardService {

    /**
     * validate card number
     *
     * @param cardNumber
     * @return of the card is valid or not.
     */
    boolean isCardValid(Long cardNumber);

    /**
     * find by card number
     *
     * @param cardNumber
     * @return a BankCard
     */
    BankCard findCard(Long cardNumber);

}
