package com.egs.BankService.data.repository;

import com.egs.BankService.data.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {

    Optional<BankCard> findOneByCardNumber(Long cardNumber);

}
