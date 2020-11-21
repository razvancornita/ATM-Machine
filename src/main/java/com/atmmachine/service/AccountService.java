package com.atmmachine.service;


import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@Slf4j
public class AccountService {

    @Autowired
    BankDao bankDao;

    public BankAccount getAccountByCardId(int cardId) throws SQLException, CardNotFoundException {
        try {
            Card card = bankDao.getCard(cardId);
            return bankDao.getAccount(card.getBankAccountId());
        } catch (CardNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }
}
