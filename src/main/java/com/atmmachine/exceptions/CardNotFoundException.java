package com.atmmachine.exceptions;


import com.atmmachine.constants.BankConstants;

public class CardNotFoundException extends Exception {
    public CardNotFoundException() {
        super(BankConstants.CARD_NOT_FOUND);
    }
}