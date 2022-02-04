package com.egs.BankService.service.api;

import com.egs.BankService.data.model.BankCard;
import com.egs.BankService.data.model.BankTransaction;
import com.egs.BankService.exception.BankTransactionException;
import org.springframework.stereotype.Service;

@Service
public interface BankTransactionService {

    Long checkBalance(BankCard bankCard) throws BankTransactionException;

    BankTransaction deposit(BankCard bankCard, Long amount) throws BankTransactionException;

    BankTransaction withdrawal(BankCard bankCard, Long amount) throws BankTransactionException;

}
