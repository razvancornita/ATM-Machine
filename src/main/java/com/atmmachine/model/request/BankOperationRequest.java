package com.atmmachine.model.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BankOperationRequest {
    OperationType operationType;
    AuthenticateRequest authenticateRequest;
    DepositOrWithdrawRequest depositOrWithdrawRequest;
}
