package com.atmmachine.model.request;

import lombok.Getter;

@Getter
public class BankOperationRequest {
    OperationType operationType;
    AuthenticateRequest authenticateRequest;
    DepositOrWithdrawRequest depositOrWithdrawRequest;
}
