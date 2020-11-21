package com.atmmachine.exceptions;

import com.atmmachine.constants.BankConstants;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super(BankConstants.INSUFFICIENT_FUNDS);
    }
}