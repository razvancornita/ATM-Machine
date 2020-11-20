package com.atmmachine.model.request;

import java.math.BigDecimal;

public class DepositOrWithdrawOperation {
    private int cardId;
    private BigDecimal amount;
    private String currency;
}
