package com.atmmachine.exceptions;

import com.atmmachine.constants.BankConstants;

public class AlreadyAuthenticatedException extends Exception {
    public AlreadyAuthenticatedException(int cardId) {
        super(BankConstants.ALREADY_AUTHENTICATED + cardId);
    }
}