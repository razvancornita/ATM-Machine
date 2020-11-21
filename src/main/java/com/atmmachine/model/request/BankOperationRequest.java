package com.atmmachine.model.request;

import lombok.Getter;

@Getter
public class BankOperationRequest {
    OperationType operationType;
    AuthenticateOperation authenticateOperation;
    DepositOrWithdrawOperation depositOrWithdrawOperation;
}
