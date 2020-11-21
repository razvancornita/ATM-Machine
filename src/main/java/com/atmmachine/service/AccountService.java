package com.atmmachine.service;


import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.exceptions.InsufficientFundsException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Card;
import com.atmmachine.model.Currency;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.DepositOrWithdrawRequest;
import com.atmmachine.model.request.OperationType;
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

    public String depositOrWithdraw(BankOperationRequest request, BankAccount bankAccount) throws InsufficientFundsException {
        DepositOrWithdrawRequest operation = request.getDepositOrWithdrawRequest();
        double amount = calculateAmount(bankAccount, operation);
        double newAmount;

        if (request.getOperationType() == OperationType.WITHDRAW) {
            if (bankAccount.getBalance() - amount < 0) throw new InsufficientFundsException();
            newAmount = bankAccount.getBalance() - amount;
        } else {
            newAmount = bankAccount.getBalance() + amount;
        }
        bankDao.changeAccountBalance(bankAccount.getId(), newAmount);
        return BankConstants.BALANCE_CHANGED_SUCCESSFULLY + newAmount;
    }

    private double calculateAmount(BankAccount bankAccount, DepositOrWithdrawRequest operation) {
        if (operation.getCurrency() == null || bankAccount.getCurrency().equals(operation.getCurrency())) {
            return operation.getAmount();
        } else {
            return calculateAmountFromDifferentCurrency(operation.getAmount(), operation.getCurrency(), bankAccount.getCurrency());
        }
    }

    private double calculateAmountFromDifferentCurrency(Double amount, Currency operationCurrency, Currency accountCurrency) {
        validateCurrency(operationCurrency);
        validateCurrency(accountCurrency);

        switch (operationCurrency) {
            case RON:
                if (accountCurrency.equals(Currency.EUR)) {
                    return amount * 0.21;
                } else if (accountCurrency.equals(Currency.USD)) {
                    return amount * 0.24;
                }
                break;
            case EUR:
                if (accountCurrency.equals(Currency.RON)) {
                    return amount * 4.87;
                } else if (accountCurrency.equals(Currency.USD)) {
                    return amount * 1.19;
                }
                break;
            case USD:
                if (accountCurrency.equals(Currency.RON)) {
                    return amount * 4.11;
                } else if (accountCurrency.equals(Currency.EUR)) {
                    return amount * 0.84;
                }
                break;
            default:
                throw new IllegalArgumentException(BankConstants.CURRENCY_NOT_SUPPORTED);
        }
        return 0;
    }

    private void validateCurrency(Currency currency) {
        if (currency != Currency.EUR && currency != Currency.RON && currency != Currency.USD)
            throw new IllegalArgumentException(BankConstants.CURRENCY_NOT_SUPPORTED);
    }
}
