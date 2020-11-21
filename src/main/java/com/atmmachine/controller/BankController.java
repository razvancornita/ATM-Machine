package com.atmmachine.controller;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.exceptions.InsufficientFundsException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.ChangePinRequest;
import com.atmmachine.service.AccountService;
import com.atmmachine.service.BankService;
import com.atmmachine.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Controller
@Slf4j
public class BankController {

    BankService bankService;
    AccountService accountService;
    CardService cardService;

    @Autowired
    public BankController(BankService bankService, AccountService accountService, CardService cardService) {
        this.bankService = bankService;
        this.accountService = accountService;
        this.cardService = cardService;
    }

    @GetMapping(value = "/atmOperation")
    public ResponseEntity<String> getBalance() {
        log.debug("received getBalance request");
        try {
            cardService.checkIfCardIsAuthenticated();
            BankAccount bankAccount = accountService.getAccountByCardId(cardService.getCardId());
            log.debug("completed getBalance request with account = {} for cardId = {}", bankAccount, cardService.getCardId());
            return ResponseEntity.ok(bankAccount.getBalance() + " " + bankAccount.getCurrency() + " left");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BankConstants.CARD_NOT_INSERTED);
        } catch (Exception e) {
            log.error("failed getBalance");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/atmOperation")
    public ResponseEntity<String> deauthenticate() {
        log.debug("received deauthenticate request");
        try {
            cardService.checkIfCardIsAuthenticated();
            cardService.deauthenticate();
            log.debug("finished deauthenticating");
            return ResponseEntity.ok(BankConstants.DEAUTHENTICATED);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BankConstants.CARD_NOT_INSERTED);
        } catch (Exception e) {
            log.error("failed deauthenticating");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/atmOperation")
    public ResponseEntity<String> atmOperation(@RequestBody BankOperationRequest request) {
        log.debug("received request {}", request.getOperationType());
        try {
            bankService.validateRequest(request);
            String message = bankService.handleOperation(request);
            log.debug("operation {} completed successfully", request.getOperationType());
            return ResponseEntity.ok(message);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AlreadyAuthenticatedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("no operation inserted");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("operation {} failed", request.getOperationType());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/atmOperation")
    public ResponseEntity<String> changePin(@RequestBody ChangePinRequest request) {
        log.debug("received changePin request");
        try {
            cardService.checkIfCardIsAuthenticated();
            cardService.changePin(request, cardService.getCardId());
            log.debug("completed changePin request");
            return ResponseEntity.ok(BankConstants.PIN_CHANGED_SUCCESS);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error("failed changePin");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
