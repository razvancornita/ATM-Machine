package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class BankService {

    @Autowired
    BankDao bankDao;


    public BankAccount getAccount(int cardId) throws SQLException {
        try {
            int bankAccountId = getBankAccountIdForCardId(cardId);
            return bankDao.getAccount(bankAccountId);
        }  catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    private int getBankAccountIdForCardId(int cardId) throws SQLException{
        try {
            return bankDao.getAccountIdForCardId(cardId);
        } catch (EmptyResultDataAccessException e) {
            throw new SQLException(BankConstants.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            throw new SQLException(BankConstants.ERR_INTERNAL);
        }
    }
}
