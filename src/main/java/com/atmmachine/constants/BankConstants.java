package com.atmmachine.constants;

public class BankConstants {

    public static final String ERR_INTERNAL = "An error occurred while getting account balance";
    public static final String ACCOUNT_NOT_FOUND = "Specified card is not associated with a bank account";
    public static final String PIN_CHANGED_SUCCESS = "PIN changed successfully";
    public static final String PIN_CHANGED_FAILED = "Failed changing PIN";
    public static final String CARD_NOT_FOUND = "The card with the specified id does not exist";
    public static final String WRONG_PIN = "PIN is incorrect";
    public static final String CARD_NOT_INSERTED = "No card was inserted. Please insert a card first";
    public static final String NO_OPERATION_INSERTED = "No operation was entered. Please enter an operation name";
    public static final String CARD_OR_PIN_NOT_COMPLETED = "The id or pin field was left empty. Please complete it";
    public static final String AMOUNT_NOT_COMPLETED = "The amount field was left empty. Please complete it";
    public static final String AUTHENTICATED = "The PIN was correct. You are now authorized with card ";
    public static final String ALREADY_AUTHENTICATED = "You are already authenticated. Please deauthenticate the card ";
    public static final String INVALID_PIN = "PIN must be numeric and must have 4 numbers";
    public static final String DEAUTHENTICATED = "You have successfully deauthenticated";
}
