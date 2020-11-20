package com.atmmachine.controller;

import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@Slf4j
public class BankController {

    @Autowired
    BankService bankService;

    @GetMapping(value = "/atmOperation/{cardId}")
    public ResponseEntity<BigDecimal> getAmount(@PathVariable("cardId") String cardId) {
        return null;
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
