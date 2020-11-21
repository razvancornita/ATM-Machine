package com.atmmachine.exceptions;

import com.atmmachine.constants.BankConstants;

public class AlreadyAuthenticatedException extends Exception {
    public AlreadyAuthenticatedException() {
        super(BankConstants.ALREADY_AUTHENTICATED);
    }
}