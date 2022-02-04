package com.egs.BankService.service.api;

import com.egs.BankService.data.model.BankCard;
import org.springframework.stereotype.Service;

@Service
public interface BankCardService {

    boolean isCardValid(Long cardNumber);
    BankCard findCard(Long cardNumber);

}
