package com.atmmachine.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankOperationRequest {
    OperationType operationType;
    AuthenticateRequest authenticateRequest;
    DepositOrWithdrawRequest depositOrWithdrawRequest;
}
