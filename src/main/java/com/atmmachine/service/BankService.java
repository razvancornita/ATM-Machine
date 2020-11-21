package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.exceptions.InsufficientFundsException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Card;
import com.atmmachine.model.request.AuthenticateRequest;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.DepositOrWithdrawRequest;
import com.atmmachine.model.request.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@Service
@Slf4j
public class BankService {
    BankDao bankDao;
    CardService cardService;
    AccountService accountService;

    @Autowired
    public BankService(BankDao bankDao, CardService cardService, AccountService accountService) {
        this.bankDao = bankDao;
        this.cardService = cardService;
        this.accountService = accountService;
    }


    public String handleOperation(BankOperationRequest request) throws CardNotFoundException, FailedLoginException, SQLException, AlreadyAuthenticatedException, InsufficientFundsException, AccessDeniedException {
        return switch (request.getOperationType()) {
            case AUTHENTICATE -> authenticate(request);
            case DEPOSIT, WITHDRAW -> {
                cardService.checkIfCardIsAuthenticated();
                BankAccount bankAccount = accountService.getAccountByCardId(cardService.getCardId());
                yield accountService.depositOrWithdraw(request, bankAccount);
            }
        };
    }


    public void validateRequest(BankOperationRequest request) {
        if (request.getOperationType() == null) {
            throw new IllegalArgumentException(BankConstants.NO_OPERATION_INSERTED);
        }

        if (request.getOperationType() == OperationType.AUTHENTICATE) {
            AuthenticateRequest authenticateRequest = request.getAuthenticateRequest();
            if (authenticateRequest.getCardId() == null || authenticateRequest.getPin() == null) {
                throw new IllegalArgumentException(BankConstants.CARD_OR_PIN_NOT_COMPLETED);
            }
        } else {
            DepositOrWithdrawRequest depositOrWithdrawRequest = request.getDepositOrWithdrawRequest();
            if (depositOrWithdrawRequest.getAmount() == null) {
                throw new IllegalArgumentException(BankConstants.AMOUNT_NOT_COMPLETED);
            }
        }
    }

    private String authenticate(BankOperationRequest request) throws FailedLoginException, CardNotFoundException, SQLException, AlreadyAuthenticatedException {
        AuthenticateRequest authenticateRequest = request.getAuthenticateRequest();
        Card card = cardService.getCard(authenticateRequest.getCardId());
        cardService.authenticate(authenticateRequest.getCardId());
        if (card.getPin().equals(authenticateRequest.getPin())) {
            return BankConstants.AUTHENTICATED + authenticateRequest.getCardId();
        } else {
            throw new FailedLoginException(BankConstants.WRONG_PIN);
        }
    }
}