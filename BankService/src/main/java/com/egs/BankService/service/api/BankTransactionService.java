package com.egs.BankService.service.api;

import com.egs.BankService.data.model.BankCard;
import com.egs.BankService.data.model.BankTransaction;
import com.egs.BankService.exception.BankTransactionException;
import org.springframework.stereotype.Service;

/**
 * Bank transaction services
 *
 * @author Amir
 */
@Service
public interface BankTransactionService {

    /**
     * check the card balance
     *
     * @param bankCard
     * @return
     * @throws BankTransactionException when there is something wrong with the transaction like when the card is blocked.
     */
    Long checkBalance(BankCard bankCard) throws BankTransactionException;

    /**
     * deposit amount into bank card.
     *
     * @param bankCard
     * @param amount
     * @return
     * @throws BankTransactionException when there is something wrong with the transaction like when the deposit amount is more than threshold.
     */
    BankTransaction deposit(BankCard bankCard, Long amount) throws BankTransactionException;

    /**
     * withdrawal amount from bank card.
     *
     * @param bankCard
     * @param amount
     * @return
     * @throws BankTransactionException when there is something wrong with the transaction like when the withdrawal amount is more than the balance amount.
     */
    BankTransaction withdrawal(BankCard bankCard, Long amount) throws BankTransactionException;

}
