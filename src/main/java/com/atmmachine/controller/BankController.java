package com.atmmachine.controller;

import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;

@Controller
@Slf4j
public class BankController {

    @Autowired
    BankService bankService;

    @GetMapping(value = "/atmOperation/{cardId}")
    public ResponseEntity<String> getBalance(@PathVariable("cardId") Integer cardId) {
        log.debug("received getBalance request for cardId = {}", cardId);
        try {
            Double accountBalance = bankService.getAccountBalance(cardId);
            String accountCurrency = bankService.getAccountCurrency(cardId);
            log.debug("completed getBalance request with balance = {} and currency = {} for cardId = {}", accountBalance, accountCurrency, cardId);
            return ResponseEntity.ok(accountBalance + " " + accountCurrency + " left");
        } catch (SQLException e) {
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
    public ResponseEntity changePin(@RequestBody BankOperationRequest request) {
        return null;
    }
}
