package com.atmmachine.constants;

public class BankConstants {
    private BankConstants() {
    }

    public static final String PIN_CHANGED_SUCCESS = "PIN changed successfully. Please reauthenticate";
    public static final String CARD_NOT_FOUND = "The card with the specified id does not exist";
    public static final String WRONG_PIN = "PIN is incorrect";
    public static final String CARD_NOT_INSERTED = "No card was inserted. Please insert a card first";
    public static final String NO_OPERATION_INSERTED = "No operation was entered. Please enter an operation name";
    public static final String CARD_OR_PIN_NOT_COMPLETED = "The id or pin field was left empty. Please complete it";
    public static final String AMOUNT_NOT_COMPLETED = "The amount field was left empty. Please complete it";
    public static final String AUTHENTICATED = "The PIN was correct. You are now authorized with card ";
    public static final String ALREADY_AUTHENTICATED = "You are already authenticated. Please deauthenticate the card ";
    public static final String INVALID_PIN = "PIN must be numeric and must have 4 digits";
    public static final String DEAUTHENTICATED = "You have successfully deauthenticated";
    public static final String CURRENCY_NOT_SUPPORTED = "The entered currency is not supported. Please enter only EUR/RON/USD";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds. Please try again";
    public static final String BALANCE_CHANGED_SUCCESSFULLY = "Operation finished successfully. Your new balance is ";
    public static final String MONEY_NOT_DIVISIBLE = "Amount not divisible by 50";
}
