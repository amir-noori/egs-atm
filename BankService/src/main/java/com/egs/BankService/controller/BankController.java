package com.egs.BankService.controller;

import com.egs.BankService.common.TransactionType;
import com.egs.BankService.common.model.BankTransactionRequest;
import com.egs.BankService.common.model.BankTransactionResponse;
import com.egs.BankService.common.model.InitializeBankCardRequest;
import com.egs.BankService.common.model.InitializeBankCardResponse;
import com.egs.BankService.data.model.BankCard;
import com.egs.BankService.data.model.BankTransaction;
import com.egs.BankService.exception.BankTransactionException;
import com.egs.BankService.service.api.BankCardService;
import com.egs.BankService.service.api.BankTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bank/service")
public class BankController {

    private static final Logger log = LogManager.getLogger(BankController.class);

    private BankCardService bankCardService;
    private BankTransactionService bankTransactionService;

    @Autowired
    public BankController(BankCardService bankCardService, BankTransactionService bankTransactionService) {
        this.bankCardService = bankCardService;
        this.bankTransactionService = bankTransactionService;
    }

    @GetMapping("/test")
    public String test() {
        return "Bank is OK.";
    }

    @PostMapping("/initialize")
    public InitializeBankCardResponse initializeBankCardConnection(@Valid @RequestBody InitializeBankCardRequest request) {
        boolean cardValid = bankCardService.isCardValid(request.getCardNumber());
        return new InitializeBankCardResponse(cardValid);
    }

    @PostMapping("/balance")
    public BankTransactionResponse checkBalance(@Valid @RequestBody BankTransactionRequest request) throws BankTransactionException {
        BankCard card = bankCardService.findCard(request.getCardNumber());
        Long balance = bankTransactionService.checkBalance(card);
        BankTransactionResponse bankTransactionResponse = new BankTransactionResponse(true, balance, TransactionType.BALANCE);
        bankTransactionResponse.setResponseMessage("Transaction Success");
        bankTransactionResponse.setResponseCode(0);
        return bankTransactionResponse;
    }

    @PostMapping("/deposit")
    public BankTransactionResponse deposit(@Valid @RequestBody BankTransactionRequest request) throws BankTransactionException {
        BankCard card = bankCardService.findCard(request.getCardNumber());
        BankTransaction bankTransaction = bankTransactionService.deposit(card, request.getAmount());
        long currentBalance = card.getBalanceAmount();
        BankTransactionResponse bankTransactionResponse =  new BankTransactionResponse(true, currentBalance, TransactionType.DEPOSIT);
        bankTransactionResponse.setResponseMessage("Transaction Success");
        bankTransactionResponse.setResponseCode(0);
        return bankTransactionResponse;
    }

    @PostMapping("/withdrawal")
    public BankTransactionResponse withdrawal(@Valid @RequestBody BankTransactionRequest request) throws BankTransactionException {
        BankCard card = bankCardService.findCard(request.getCardNumber());
        BankTransaction bankTransaction = bankTransactionService.withdrawal(card, request.getAmount());
        long currentBalance = card.getBalanceAmount();
        BankTransactionResponse bankTransactionResponse = new BankTransactionResponse(true, currentBalance, TransactionType.WITHDRAWAL);
        bankTransactionResponse.setResponseMessage("Transaction Success");
        bankTransactionResponse.setResponseCode(0);
        return bankTransactionResponse;
    }


}
