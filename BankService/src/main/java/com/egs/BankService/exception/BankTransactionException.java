package com.egs.BankService.exception;

import com.egs.BankService.common.TransactionType;
import com.egs.BankService.data.model.BankCard;

/**
 * This exception is thrown when there is an issue with a bank transaction.
 *
 * @author Amir
 */
public class BankTransactionException extends Exception {

    private String description;
    private Integer errorCode;
    private TransactionType transactionType;
    private BankCard bankCard;

    public BankTransactionException(Integer errorCode, String description, TransactionType transactionType, BankCard bankCard) {
        super();
        this.description = description;
        this.errorCode = errorCode;
        this.transactionType = transactionType;
        this.bankCard = bankCard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }


    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }
}
