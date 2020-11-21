package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class BankService {

    @Autowired
    BankDao bankDao;

    public BigDecimal getAccountBalance(int cardId) throws SQLException {
        try {
            int bankAccountId = getBankAccountIdForCardId(cardId);
            return bankDao.getAccountBalance(bankAccountId);
        }  catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    public String getAccountCurrency(Integer cardId) throws SQLException {
        try {
            int bankAccountId = getBankAccountIdForCardId(cardId);
            return bankDao.getAccountCurrency(bankAccountId);
        }  catch (Exception e) {
            throw new SQLException(BankConstants.ERR_INTERNAL);
        }
    }

    private int getBankAccountIdForCardId(int cardId) throws SQLException{
        try {
            return bankDao.getAccountIdForCardId(cardId);
        } catch (EmptyResultDataAccessException e) {
            throw new SQLException("Specified card is not associated with a bank account");
        } catch (Exception e) {
            throw new SQLException(BankConstants.ERR_INTERNAL);
        }
    }
}
