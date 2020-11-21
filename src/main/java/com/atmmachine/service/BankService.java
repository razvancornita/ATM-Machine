package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.model.Card;
import com.atmmachine.model.request.AuthenticateOperation;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.DepositOrWithdrawOperation;
import com.atmmachine.model.request.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.sql.SQLException;

@Service
@Slf4j
public class BankService {

    @Autowired
    BankDao bankDao;

    @Autowired
    CardService cardService;


    public String handleOperation(BankOperationRequest request) throws CardNotFoundException, FailedLoginException, SQLException, AlreadyAuthenticatedException {
        validateRequest(request);

        switch (request.getOperationType()) {
            case AUTHENTICATE -> {
                return authenticate(request);
            }
            case DEPOSIT -> bankDao.deposit();
            case WITHDRAW -> bankDao.withdraw();
        }
        return null;
    }


    private void validateRequest(BankOperationRequest request) {
        if (request.getOperationType() == null) {
            throw new IllegalArgumentException(BankConstants.NO_OPERATION_INSERTED);
        }

        if (request.getOperationType() == OperationType.AUTHENTICATE) {
            AuthenticateOperation authenticateOperation = request.getAuthenticateOperation();
            if (authenticateOperation.getCardId() == null || authenticateOperation.getPin() == null) {
                throw new IllegalArgumentException(BankConstants.CARD_OR_PIN_NOT_COMPLETED);
            }
        } else {
            DepositOrWithdrawOperation depositOrWithdrawOperation = request.getDepositOrWithdrawOperation();
            if (depositOrWithdrawOperation.getAmount() == null) {
                throw new IllegalArgumentException(BankConstants.AMOUNT_NOT_COMPLETED);
            }
        }
    }

    private String authenticate(BankOperationRequest request) throws FailedLoginException, CardNotFoundException, SQLException, AlreadyAuthenticatedException {
        AuthenticateOperation authenticateOperation = request.getAuthenticateOperation();
        Card card = cardService.getCard(authenticateOperation.getCardId());
        cardService.authenticate(authenticateOperation.getCardId());
        if (card.getPin().equals(authenticateOperation.getPin())) {

            return BankConstants.AUTHENTICATED + authenticateOperation.getCardId();
        } else {
            throw new FailedLoginException(BankConstants.WRONG_PIN);
        }
    }
}
