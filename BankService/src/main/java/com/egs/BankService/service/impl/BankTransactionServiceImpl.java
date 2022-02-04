package com.egs.BankService.service.impl;

import com.egs.BankService.common.TransactionType;
import com.egs.BankService.data.model.BankCard;
import com.egs.BankService.data.model.BankTransaction;
import com.egs.BankService.data.repository.BankCardRepository;
import com.egs.BankService.data.repository.BankTransactionRepository;
import com.egs.BankService.exception.BankTransactionException;
import com.egs.BankService.service.api.BankTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class BankTransactionServiceImpl implements BankTransactionService {

    private static final Logger log = LogManager.getLogger(BankTransactionServiceImpl.class);


    private BankTransactionRepository bankTransactionRepository;
    private BankCardRepository bankCardRepository;
    private Environment environment;

    @Autowired
    public BankTransactionServiceImpl(BankTransactionRepository bankTransactionRepository,
                                      BankCardRepository bankCardRepository,
                                      Environment environment) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.bankCardRepository = bankCardRepository;
        this.environment = environment;
    }

    @Transactional
    @Override
    public Long checkBalance(BankCard bankCard) throws BankTransactionException {
        try {
            BankCard card = checkCard(bankCard, TransactionType.BALANCE);
            BankTransaction transaction = createTransaction(card, 0, TransactionType.BALANCE);

            bankTransactionRepository.save(transaction);

            return card.getBalanceAmount();
        } catch (BankTransactionException e) {
            log.debug("Issue with transaction: " + e.getDescription());
            throw e;
        } catch (Exception e) {
            throw new BankTransactionException(100, e.getMessage(), TransactionType.BALANCE, bankCard);
        }
    }

    @Transactional
    @Override
    public BankTransaction deposit(BankCard bankCard, Long amount) throws BankTransactionException {
        try {
            BankCard card = checkCard(bankCard, TransactionType.DEPOSIT);
            BankTransaction transaction = createTransaction(card, amount, TransactionType.DEPOSIT);
            setCardBalance(card, amount, TransactionType.DEPOSIT);
            bankTransactionRepository.save(transaction);

            return transaction;
        } catch (BankTransactionException e) {
            log.debug("Issue with transaction: " + e.getDescription());
            throw e;
        } catch (Exception e) {
            throw new BankTransactionException(100, e.getMessage(), TransactionType.DEPOSIT, bankCard);
        }
    }

    @Transactional
    @Override
    public BankTransaction withdrawal(BankCard bankCard, Long amount) throws BankTransactionException {
        try {
            BankCard card = checkCard(bankCard, TransactionType.WITHDRAWAL);
            BankTransaction transaction = createTransaction(card, amount, TransactionType.WITHDRAWAL);
            setCardBalance(card, amount, TransactionType.WITHDRAWAL);
            bankTransactionRepository.save(transaction);

            return transaction;
        } catch (BankTransactionException e) {
            log.debug("Issue with transaction: " + e.getDescription());
            throw e;
        } catch (Exception e) {
            throw new BankTransactionException(100, e.getMessage(), TransactionType.WITHDRAWAL, bankCard);
        }
    }

    private BankTransaction createTransaction(BankCard card, long amount, TransactionType transactionType) {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        BankTransaction transaction = new BankTransaction();
        transaction.setBankCard(card);
        transaction.setCreateTime(currentTime);
        transaction.setModifyTime(currentTime);
        transaction.setSuccessful(true);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);

        return transaction;
    }

    private void setCardBalance(BankCard card, Long amount, TransactionType transactionType) throws BankTransactionException {
        String depositThresholdProperty = environment.getProperty("bank.deposit.threshold");
        String withdrawalThresholdProperty = environment.getProperty("bank.withdrawal.threshold");

        Long depositThreshold = Long.parseLong(depositThresholdProperty);
        Long withdrawalThreshold = Long.parseLong(withdrawalThresholdProperty);


        long balanceAmount = card.getBalanceAmount();
        switch (transactionType) {
            case BALANCE:
                throw new BankTransactionException(102, "Cannot set balance in check balance!", transactionType, card);
            case DEPOSIT:
                if (amount < 0) {
                    throw new BankTransactionException(103, "Negative deposit!", transactionType, card);
                } else if (amount > depositThreshold) {
                    throw new BankTransactionException(104, "Deposit amount is more than threshold!", transactionType, card);
                }
                card.setBalanceAmount(balanceAmount + amount);
                bankCardRepository.save(card);
                break;
            case WITHDRAWAL:
                if (balanceAmount - amount < 0) {
                    throw new BankTransactionException(105, "Card do not have enough balance!", transactionType, card);
                } else if (amount > withdrawalThreshold) {
                    throw new BankTransactionException(106, "Withdrawal amount is more than threshold!", transactionType, card);
                }
                card.setBalanceAmount(balanceAmount - amount);
                bankCardRepository.save(card);
                break;
        }

    }

    private BankCard checkCard(BankCard requestedCard, TransactionType transactionType) throws BankTransactionException {
        Optional<BankCard> oneByCardNumber = bankCardRepository.findOneByCardNumber(requestedCard.getCardNumber());
        if (oneByCardNumber.isEmpty()) {
            throw new BankTransactionException(107, "No such card!", transactionType, requestedCard);
        } else {
            BankCard card = oneByCardNumber.get();
            if (card.getBlocked()) {
                throw new BankTransactionException(108, "Card is blocked!", transactionType, requestedCard);
            } else {
                return card;
            }
        }
    }

}
