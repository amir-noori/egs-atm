package com.egs.BankService.service.impl;

import com.egs.BankService.data.model.BankCard;
import com.egs.BankService.data.repository.BankCardRepository;
import com.egs.BankService.service.api.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankCardServiceImpl implements BankCardService {

    private static final Logger log = LogManager.getLogger(BankCardServiceImpl.class);


    private BankCardRepository bankCardRepository;

    @Autowired
    public BankCardServiceImpl(BankCardRepository bankCardRepository) {
        this.bankCardRepository = bankCardRepository;
    }

    @Override
    public boolean isCardValid(Long cardNumber) {
        Optional<BankCard> oneByCardNumber = bankCardRepository.findOneByCardNumber(cardNumber);

        if (oneByCardNumber.isEmpty()) return false;
        BankCard bankCard = oneByCardNumber.get();
        if (bankCard.getBlocked()) return false;

        return true;
    }

    @Override
    public BankCard findCard(Long cardNumber) {
        Optional<BankCard> oneByCardNumber = bankCardRepository.findOneByCardNumber(cardNumber);
        return oneByCardNumber.get();
    }
}
