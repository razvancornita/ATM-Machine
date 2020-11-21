package com.atmmachine.exceptions;


import com.atmmachine.constants.BankConstants;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
        super(BankConstants.ACCOUNT_NOT_FOUND);
    }
}