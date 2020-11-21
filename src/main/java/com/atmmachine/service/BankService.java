package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Card;
import com.atmmachine.model.request.ChangePinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class BankService {

    @Autowired
    BankDao bankDao;


    public BankAccount getAccount(int cardId) throws SQLException {
        try {
            int bankAccountId = getBankAccountIdForCardId(cardId);
            return bankDao.getAccount(bankAccountId);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    private int getBankAccountIdForCardId(int cardId) throws SQLException {
        try {
            return bankDao.getAccountIdForCardId(cardId);
        } catch (EmptyResultDataAccessException e) {
            throw new SQLException(BankConstants.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            throw new SQLException(BankConstants.ERR_INTERNAL);
        }
    }

    public void changePin(ChangePinRequest request) throws SQLException {
        try {
            Card card = bankDao.getCard(request.getCardId());
            if (card.getPin() != request.getOldPin()) {
                throw new IllegalArgumentException(BankConstants.WRONG_PIN);
            } else {
                bankDao.changeCardPin(request.getCardId(), request.getNewPin());
            }
        } catch (EmptyResultDataAccessException e) {
            throw new SQLException(BankConstants.CARD_NOT_FOUND);
        }
    }
}
