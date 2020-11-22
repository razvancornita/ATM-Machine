package com.atmmachine.util;

import com.atmmachine.model.Currency;
import com.atmmachine.model.request.*;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class RequestGenerator {

    public static ChangePinRequest generateChangePinRequest() {
        return new ChangePinRequest("3333", "4444");
    }

    public static BankOperationRequest generateWithdrawRequest(double amount) {
        return new BankOperationRequest(OperationType.WITHDRAW, null,
                new DepositOrWithdrawRequest(amount, Currency.EUR));
    }

    public static BankOperationRequest generateDepositRequest(double amount) {
        return new BankOperationRequest(OperationType.DEPOSIT, null,
                new DepositOrWithdrawRequest(amount, Currency.EUR));
    }

    public static BankOperationRequest generateAuthenticateRequest() {
        return new BankOperationRequest(OperationType.AUTHENTICATE, new AuthenticateRequest(1, "1234"), null);
    }


    public static MediaType generateJsonMediaType() {
        return new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    }

    public static BankOperationRequest generateValidRequest() {
        return null;
    }
}
