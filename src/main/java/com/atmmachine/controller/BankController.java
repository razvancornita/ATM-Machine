package com.atmmachine.controller;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.ChangePinRequest;
import com.atmmachine.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class BankController {

    @Autowired
    BankService bankService;

    @GetMapping(value = "/atmOperation/{cardId}")
    public ResponseEntity<String> getBalance(@PathVariable("cardId") Integer cardId) {
        log.debug("received getBalance request for cardId = {}", cardId);
        try {
            BankAccount bankAccount = bankService.getAccount(cardId);
            log.debug("completed getBalance request with account = {} for cardId = {}", bankAccount, cardId);
            return ResponseEntity.ok(bankAccount.getBalance() + " " + bankAccount.getCurrency() + " left");
        } catch (Exception e) {
            log.error("failed getBalance for cardId = {}", cardId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/atmOperation/{cardId}")
    public ResponseEntity<String> deleteOperation(@PathVariable("cardId") String cardId) {
        return null;
    }

    @PostMapping(value = "/atmOperation")
    public ResponseEntity getOperation(@RequestBody BankOperationRequest request) {
        return null;
    }

    @PutMapping(value = "/atmOperation")
    public ResponseEntity changePin(@RequestBody ChangePinRequest request) {
        log.debug("received changePin request");
        try {
            bankService.changePin(request);
            log.debug("completed changePin request");
            return ResponseEntity.ok(BankConstants.PIN_CHANGED_SUCCESS);
        } catch (Exception e) {
            log.error("failed changePin");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
