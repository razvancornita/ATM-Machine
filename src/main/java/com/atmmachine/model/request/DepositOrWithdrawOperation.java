package com.atmmachine.model.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DepositOrWithdrawOperation {
    private BigDecimal amount;
    private String currency;
}
